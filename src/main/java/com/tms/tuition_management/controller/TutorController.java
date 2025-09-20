package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.TutorService;
import com.tms.tuition_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TutorController {

    private final TutorService tutorService;
    private final UserService userService;
    private final UserRepository userRepository;

    public TutorController(TutorService tutorService, UserService userService, UserRepository userRepository) {
        this.tutorService = tutorService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/tutors")
    public String listTutors(Model model) {
        model.addAttribute("tutors", tutorService.getAllTutors());
        return "tutors";
    }

    @GetMapping("/tutors/new")
    public String createTutorForm(Model model) {
        model.addAttribute("tutor", new Tutor());
        return "create_tutor";
    }

    @PostMapping("/tutors")
    public String saveTutor(@ModelAttribute("tutor") Tutor tutor, @RequestParam String password) {
        userService.createTutorUser(tutor, password);
        return "redirect:/tutors";
    }

    @GetMapping("/tutors/edit/{id}")
    public String editTutorForm(@PathVariable Long id, Model model) {
        model.addAttribute("tutor", tutorService.getTutorById(id));
        return "edit_tutor";
    }

    @PostMapping("/tutors/{id}")
    public String updateTutor(@PathVariable Long id, @ModelAttribute("tutor") Tutor tutorDetails) {
        Tutor existingTutor = tutorService.getTutorById(id);
        existingTutor.setName(tutorDetails.getName());
        existingTutor.setSubject(tutorDetails.getSubject());

        User user = existingTutor.getUser();
        if (user != null) {
            user.setEmail(tutorDetails.getEmail());
            userRepository.save(user);
        }

        tutorService.saveTutor(existingTutor);
        return "redirect:/tutors";
    }

    @GetMapping("/tutors/delete/{id}")
    public String deleteTutor(@PathVariable Long id) {
        tutorService.deleteTutorById(id);
        return "redirect:/tutors";
    }
}