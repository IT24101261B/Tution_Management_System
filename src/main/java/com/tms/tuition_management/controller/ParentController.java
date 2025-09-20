package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Parent;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.service.ParentService;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ParentController {

    private final ParentService parentService;
    private final StudentService studentService;
    private final UserService userService;

    public ParentController(ParentService parentService, StudentService studentService, UserService userService) {
        this.parentService = parentService;
        this.studentService = studentService;
        this.userService = userService;
    }

    @GetMapping("/parents")
    public String listParents(Model model) {
        model.addAttribute("parents", parentService.findAll());
        return "parents";
    }

    @GetMapping("/parents/new")
    public String showCreateParentForm(Model model) {
        model.addAttribute("parent", new Parent());
        model.addAttribute("students", studentService.getAllStudents());
        return "create_parent";
    }

    @PostMapping("/parents")
    public String saveParent(@ModelAttribute("parent") Parent parent, @RequestParam String password, @RequestParam List<Long> studentIds) {
        userService.createParentUser(parent, password, studentIds);
        return "redirect:/parents";
    }

    @GetMapping("/parents/edit/{id}")
    public String editParentForm(@PathVariable Long id, Model model) {
        model.addAttribute("parent", parentService.findById(id));
        model.addAttribute("students", studentService.getAllStudents());
        return "edit_parent";
    }

    @PostMapping("/parents/{id}")
    public String updateParent(@PathVariable Long id, @ModelAttribute("parent") Parent parent, @RequestParam List<Long> studentIds) {
        Parent existingParent = parentService.findById(id);
        existingParent.setName(parent.getName());
        existingParent.setEmail(parent.getEmail());
        existingParent.setPhone(parent.getPhone());

        Set<Student> linkedStudents = studentService.getAllStudents().stream()
                .filter(s -> studentIds.contains(s.getId()))
                .collect(Collectors.toSet());
        existingParent.setStudents(linkedStudents);

        parentService.save(existingParent);
        return "redirect:/parents";
    }

    @GetMapping("/parents/delete/{id}")
    public String deleteParent(@PathVariable Long id) {
        parentService.deleteById(id);
        return "redirect:/parents";
    }
}