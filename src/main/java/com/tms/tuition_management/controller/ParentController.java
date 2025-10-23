package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Parent;
import com.tms.tuition_management.model.User; // Ensure User is imported
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.ParentService;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.UserService;
import jakarta.validation.Valid; // Add this import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Add this import
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ParentController {

    private final ParentService parentService;
    private final UserService userService;
    private final StudentService studentService;
    private final UserRepository userRepository;

    public ParentController(ParentService parentService, UserService userService, StudentService studentService, UserRepository userRepository) {
        this.parentService = parentService;
        this.userService = userService;
        this.studentService = studentService;
        this.userRepository = userRepository;
    }

    @GetMapping("/parents")
    public String listParents(Model model) {
        model.addAttribute("parents", parentService.getAllParents());
        return "parents";
    }

    @GetMapping("/parents/new")
    public String createParentForm(Model model) {
        model.addAttribute("parent", new Parent());
        model.addAttribute("allStudents", studentService.getAllStudents());
        return "create_parent";
    }

    @PostMapping("/parents")
    public String saveParent(@Valid @ModelAttribute("parent") Parent parent, // Add @Valid
                             BindingResult bindingResult, // Add BindingResult
                             @RequestParam String password,
                             @RequestParam(required = false) List<Long> studentIds, Model model) {
        if (parent.getEmail() != null && userService.findByEmail(parent.getEmail()) != null) {
            bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("parent", parent);
            model.addAttribute("allStudents", studentService.getAllStudents());
            return "create_parent";
        }
        userService.createParentUser(parent, password, studentIds);
        return "redirect:/parents?success";
    }

    @GetMapping("/parents/edit/{id}")
    public String editParentForm(@PathVariable Long id, Model model) {
        model.addAttribute("parent", parentService.getParentById(id));
        model.addAttribute("allStudents", studentService.getAllStudents());
        return "edit_parent";
    }

    @PostMapping("/parents/{id}")
    public String updateParent(@PathVariable Long id,
                               @Valid @ModelAttribute("parent") Parent parentDetails, // Add @Valid
                               BindingResult bindingResult, // Add BindingResult
                               @RequestParam(required = false) List<Long> studentIds, Model model) {

        Parent existingParent = parentService.getParentById(id);

        // Check if email is being changed and if the new email already exists for another user
        if (existingParent.getUser() != null && parentDetails.getEmail() != null &&
                !existingParent.getUser().getEmail().equals(parentDetails.getEmail())) {
            User userWithNewEmail = userService.findByEmail(parentDetails.getEmail());
            if (userWithNewEmail != null && !userWithNewEmail.getId().equals(existingParent.getUser().getId())) {
                bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
            }
        }

        if (bindingResult.hasErrors()) {
            parentDetails.setId(id); // Keep the ID
            if(parentDetails.getUser() == null) parentDetails.setUser(existingParent.getUser()); // Repopulate User if needed
            model.addAttribute("parent", parentDetails);
            model.addAttribute("allStudents", studentService.getAllStudents());
            return "edit_parent";
        }

        // Update logic
        existingParent.setName(parentDetails.getName());
        existingParent.setPhone(parentDetails.getPhone());
        if (existingParent.getUser() != null && parentDetails.getEmail() != null) {
            existingParent.getUser().setEmail(parentDetails.getEmail());
            userRepository.save(existingParent.getUser());
        }
        parentService.updateParentStudents(existingParent, studentIds);
        parentService.saveParent(existingParent);

        return "redirect:/parents?updateSuccess";
    }

    @GetMapping("/parents/delete/{id}")
    public String deleteParent(@PathVariable Long id) {
        parentService.deleteParentById(id);
        return "redirect:/parents?deleteSuccess";
    }
}