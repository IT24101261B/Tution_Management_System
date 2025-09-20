package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Attendance;
import com.tms.tuition_management.model.AttendanceStatus;
import com.tms.tuition_management.model.Schedule;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.service.AttendanceService;
import com.tms.tuition_management.service.ScheduleService;
import com.tms.tuition_management.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AttendanceController {

    private final ScheduleService scheduleService;
    private final AttendanceService attendanceService;
    private final StudentService studentService;

    public AttendanceController(ScheduleService scheduleService, AttendanceService attendanceService, StudentService studentService) {
        this.scheduleService = scheduleService;
        this.attendanceService = attendanceService;
        this.studentService = studentService;
    }

    @GetMapping("/attendance")
    public String showSchedulesForAttendance(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "attendance_schedules";
    }

    @GetMapping("/attendance/mark/{scheduleId}")
    public String showAttendanceForm(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        model.addAttribute("schedule", schedule);
        model.addAttribute("students", schedule.getStudents());
        model.addAttribute("statuses", AttendanceStatus.values());

        List<Attendance> existingRecords = attendanceService.findByScheduleId(scheduleId);
        model.addAttribute("existingRecords", existingRecords.stream()
                .collect(Collectors.toMap(a -> a.getStudent().getId(), Attendance::getStatus)));

        return "mark_attendance";
    }

    @PostMapping("/attendance/save")
    public String saveAttendance(@RequestParam Long scheduleId, @RequestParam Map<String, String> attendanceData) {
        attendanceService.deleteByScheduleId(scheduleId);

        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        List<Attendance> attendanceList = new ArrayList<>();
        for (Map.Entry<String, String> entry : attendanceData.entrySet()) {
            if (entry.getKey().startsWith("status_")) {
                Long studentId = Long.parseLong(entry.getKey().substring(7));
                Student student = studentService.getStudentById(studentId);
                AttendanceStatus status = AttendanceStatus.valueOf(entry.getValue());

                Attendance attendance = new Attendance();
                attendance.setStudent(student);
                attendance.setSchedule(schedule);
                attendance.setStatus(status);
                attendance.setAttendanceDate(LocalDate.now());
                attendanceList.add(attendance);
            }
        }
        attendanceService.saveAll(attendanceList);
        return "redirect:/attendance?success";
    }

    @GetMapping("/attendance/delete/{scheduleId}")
    public String deleteAttendance(@PathVariable Long scheduleId) {
        attendanceService.deleteByScheduleId(scheduleId);
        return "redirect:/attendance?deleteSuccess";
    }
}