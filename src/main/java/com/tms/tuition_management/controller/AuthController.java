package com.tms.tuition_management.controller;

import com.tms.tuition_management.dto.UserDto;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.service.UserService;
import jakarta.validation.Valid; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("userDto") UserDto userDto,
                               BindingResult bindingResult, 
                               Model model) {
        
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            bindingResult.rejectValue("email", null, "An account with this email already exists.");
        }

        
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto); 
            return "register"; 
        }

        
        userService.registerNewUser(
                userDto.getName(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getSubject(),
                userDto.getPhone(),
                userDto.getChildEmail()
        );
        return "redirect:/register?success";
    }
}
