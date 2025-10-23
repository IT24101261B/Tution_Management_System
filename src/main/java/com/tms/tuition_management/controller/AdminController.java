package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.ParentService;
import com.tms.tuition_management.service.ScheduleService;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.TutorService;
import com.tms.tuition_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final TutorService tutorService;
    private final ParentService parentService;
    private final ScheduleService scheduleService;

    public AdminController(UserService userService, UserRepository userRepository, StudentService studentService, TutorService tutorService, ParentService parentService, ScheduleService scheduleService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.tutorService = tutorService;
        this.parentService = parentService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("pendingUsers", userRepository.findByEnabledFalse());
        model.addAttribute("studentCount", studentService.countStudents());
        model.addAttribute("tutorCount", tutorService.countTutors());
        // FIX: Use getAllParents() instead of findAll()
        model.addAttribute("parentCount", parentService.getAllParents().size());
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        return "admin_dashboard";
    }

    @PostMapping("/admin/approve/{id}")
    @Transactional
    public String approveUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
        }
        return "redirect:/admin/dashboard";
    }
}