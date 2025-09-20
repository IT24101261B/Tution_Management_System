package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.User;
import com.tms.tuition_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String role,
                               @RequestParam(required = false) String subject,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String childEmail) {
        User existingUser = userService.findByEmail(email);
        if (existingUser != null) {
            return "redirect:/register?registerFail";
        }
        userService.registerNewUser(name, email, password, role, subject, phone, childEmail);
        return "redirect:/login?registerSuccess";
    }
}