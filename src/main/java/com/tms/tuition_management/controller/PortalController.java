package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.*;
import com.tms.tuition_management.repository.ParentRepository;
import com.tms.tuition_management.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class PortalController {

    private final ParentService parentService;
    private final AttendanceService attendanceService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final ParentRepository parentRepository;

    public PortalController(ParentService parentService, AttendanceService attendanceService, ScheduleService scheduleService, UserService userService, ParentRepository parentRepository) {
        this.parentService = parentService;
        this.attendanceService = attendanceService;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.parentRepository = parentRepository;
    }

    @GetMapping("/portal")
    public String parentPortalRedirect(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Parent parent = parentRepository.findByUserId(user.getId());
        return "redirect:/portal/parent/" + parent.getId();
    }

    @GetMapping("/portal/parent/{parentId}")
    public String showParentPortal(@PathVariable Long parentId, Model model) {
        Parent parent = parentService.findById(parentId);
        model.addAttribute("parent", parent);
        model.addAttribute("attendanceMap", parent.getStudents().stream()
                .collect(Collectors.toMap(
                        student -> student,
                        student -> attendanceService.findByStudentId(student.getId())
                )));

        Map<Student, Set<Tutor>> tutorMap = parent.getStudents().stream()
                .collect(Collectors.toMap(
                        student -> student,
                        student -> scheduleService.findSchedulesByStudentId(student.getId()).stream()
                                .map(Schedule::getTutor).collect(Collectors.toSet())
                ));
        model.addAttribute("tutorMap", tutorMap);

        return "parent_portal";
    }
}