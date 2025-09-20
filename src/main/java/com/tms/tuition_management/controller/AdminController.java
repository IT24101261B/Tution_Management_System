package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final StudentService studentService;
    private final TutorService tutorService;
    private final ParentService parentService;
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminController(StudentService studentService, TutorService tutorService, ParentService parentService, ScheduleService scheduleService, UserRepository userRepository, UserService userService) {
        this.studentService = studentService;
        this.tutorService = tutorService;
        this.parentService = parentService;
        this.scheduleService = scheduleService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("tutorCount", tutorService.getAllTutors().size());
        model.addAttribute("parentCount", parentService.findAll().size());
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        model.addAttribute("pendingUsers", userRepository.findByEnabledFalse());
        return "admin_dashboard";
    }

    @PostMapping("/admin/approve/{userId}")
    public String approveUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
        }
        return "redirect:/admin/dashboard";
    }
}