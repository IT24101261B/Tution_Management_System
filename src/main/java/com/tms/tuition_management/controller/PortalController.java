package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Attendance;
import com.tms.tuition_management.model.Parent;
import com.tms.tuition_management.model.Schedule;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.service.AttendanceService;
import com.tms.tuition_management.service.ParentService;
import com.tms.tuition_management.service.ScheduleService;
import com.tms.tuition_management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PortalController {

    private final ParentService parentService;
    private final AttendanceService attendanceService;
    private final ScheduleService scheduleService;
    private final UserService userService;

    public PortalController(ParentService parentService, AttendanceService attendanceService, ScheduleService scheduleService, UserService userService) {
        this.parentService = parentService;
        this.attendanceService = attendanceService;
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    @GetMapping("/portal")
    public String parentPortal(Model model, Authentication authentication) {
        String email = authentication.getName();
        Long userId = userService.findByEmail(email).getId();
        // FIX: Use getParentById if you are passing the parent's ID,
        // or findByUserId if you are passing the user's ID.
        // Assuming you need to find the parent linked to the logged-in user:
        Parent parent = parentService.findByUserId(userId);

        if (parent == null) {
            // Handle case where parent profile not found for the user
            return "redirect:/login?error=parent_profile_not_found";
        }

        model.addAttribute("parent", parent);

        Map<Long, List<Attendance>> attendanceMap = new HashMap<>();
        Map<Long, Map<Tutor, String>> tutorMap = new HashMap<>(); // Map<StudentId, Map<Tutor, SubjectName>>

        for (Student student : parent.getStudents()) {
            attendanceMap.put(student.getId(), attendanceService.findByStudentId(student.getId()));

            Map<Tutor, String> studentTutors = scheduleService.findSchedulesByStudentId(student.getId()).stream()
                    .filter(schedule -> schedule.getTutor() != null)
                    .collect(Collectors.toMap(Schedule::getTutor, Schedule::getSubjectName, (existing, replacement) -> existing)); // Avoid duplicate tutors if they teach multiple subjects
            tutorMap.put(student.getId(), studentTutors);
        }

        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("tutorMap", tutorMap);

        return "parent_portal";
    }
}