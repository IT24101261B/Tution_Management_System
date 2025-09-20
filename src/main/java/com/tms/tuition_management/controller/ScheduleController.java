package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Schedule;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.service.ScheduleService;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.TutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final TutorService tutorService;
    private final StudentService studentService;

    public ScheduleController(ScheduleService scheduleService, TutorService tutorService, StudentService studentService) {
        this.scheduleService = scheduleService;
        this.tutorService = tutorService;
        this.studentService = studentService;
    }

    @GetMapping("/schedules")
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "schedules";
    }

    @GetMapping("/schedules/new")
    public String showCreateScheduleForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("tutors", tutorService.getAllTutors());
        model.addAttribute("students", studentService.getAllStudents());
        return "create_schedule";
    }

    @PostMapping("/schedules")
    public String saveSchedule(@ModelAttribute("schedule") Schedule schedule,
                               @RequestParam("tutorId") Long tutorId,
                               @RequestParam("studentIds") List<Long> studentIds) {
        Tutor tutor = tutorService.getTutorById(tutorId);
        schedule.setTutor(tutor);
        Set<Student> enrolledStudents = studentService.getAllStudents().stream()
                .filter(s -> studentIds.contains(s.getId())).collect(Collectors.toSet());
        schedule.setStudents(enrolledStudents);
        scheduleService.saveSchedule(schedule);
        return "redirect:/schedules";
    }

    @GetMapping("/schedules/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getScheduleById(id));
        model.addAttribute("tutors", tutorService.getAllTutors());
        model.addAttribute("students", studentService.getAllStudents());
        return "edit_schedule";
    }

    @PostMapping("/schedules/update/{id}")
    public String updateSchedule(@PathVariable Long id, @ModelAttribute("schedule") Schedule scheduleDetails,
                                 @RequestParam("tutorId") Long tutorId,
                                 @RequestParam("studentIds") List<Long> studentIds) {
        Schedule existingSchedule = scheduleService.getScheduleById(id);
        existingSchedule.setSubjectName(scheduleDetails.getSubjectName());
        existingSchedule.setDateTime(scheduleDetails.getDateTime());
        existingSchedule.setLocation(scheduleDetails.getLocation());

        Tutor tutor = tutorService.getTutorById(tutorId);
        existingSchedule.setTutor(tutor);

        Set<Student> enrolledStudents = studentService.getAllStudents().stream()
                .filter(s -> studentIds.contains(s.getId())).collect(Collectors.toSet());
        existingSchedule.setStudents(enrolledStudents);

        scheduleService.saveSchedule(existingSchedule);
        return "redirect:/schedules";
    }

    @GetMapping("/schedules/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteScheduleById(id);
        return "redirect:/schedules";
    }
}