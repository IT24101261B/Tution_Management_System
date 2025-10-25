package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.service.LessonService;
import com.tms.tuition_management.service.ScheduleService;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.TutorService;
import com.tms.tuition_management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserService userService;
    private final StudentService studentService;
    private final TutorService tutorService;
    private final ScheduleService scheduleService;
    private final LessonService lessonService; // The service for lessons

    public DashboardController(UserService userService, StudentService studentService, TutorService tutorService, ScheduleService scheduleService, LessonService lessonService) {
        this.userService = userService;
        this.studentService = studentService;
        this.tutorService = tutorService;
        this.scheduleService = scheduleService;
        this.lessonService = lessonService;
    }

    @GetMapping("/tutor/dashboard")
    public String tutorDashboard(Model model, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Tutor tutor = tutorService.findByUserId(user.getId());
        if (tutor != null) {
            model.addAttribute("schedules", scheduleService.findSchedulesByTutorId(tutor.getId()));
            model.addAttribute("tutor", tutor);
        }
        return "tutor_dashboard";
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        Student student = studentService.findByUserId(user.getId());

        if (student != null) {
            model.addAttribute("schedules", scheduleService.findSchedulesByStudentId(student.getId()));
            model.addAttribute("student", student);
            // This line will now work correctly
            model.addAttribute("lessons", lessonService.getAllLessons());
        }
        return "student_dashboard";
    }
}
