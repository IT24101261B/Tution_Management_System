package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.model.User; // Ensure User is imported
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.TutorService;
import com.tms.tuition_management.service.UserService;
import jakarta.validation.Valid; // Add this import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Add this import
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
    public String saveTutor(@Valid @ModelAttribute("tutor") Tutor tutor, // Add @Valid
                            BindingResult bindingResult, // Add BindingResult
                            @RequestParam String password, Model model) {
        if (tutor.getEmail() != null && userService.findByEmail(tutor.getEmail()) != null) {
            bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("tutor", tutor);
            return "create_tutor";
        }
        userService.createTutorUser(tutor, password);
        return "redirect:/tutors?success";
    }

    @GetMapping("/tutors/edit/{id}")
    public String editTutorForm(@PathVariable Long id, Model model) {
        model.addAttribute("tutor", tutorService.getTutorById(id));
        return "edit_tutor";
    }

    @PostMapping("/tutors/{id}")
    public String updateTutor(@PathVariable Long id,
                              @Valid @ModelAttribute("tutor") Tutor tutorDetails, // Add @Valid
                              BindingResult bindingResult, // Add BindingResult
                              Model model) {

        Tutor existingTutor = tutorService.getTutorById(id);

        // Check if email is changed and if the new email already exists for another user
        if (existingTutor.getUser() != null && tutorDetails.getEmail() != null &&
                !existingTutor.getUser().getEmail().equals(tutorDetails.getEmail())) {
            User userWithNewEmail = userService.findByEmail(tutorDetails.getEmail());
            if (userWithNewEmail != null && !userWithNewEmail.getId().equals(existingTutor.getUser().getId())) {
                bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
            }
        }

        if (bindingResult.hasErrors()) {
            tutorDetails.setId(id); // Keep the ID
            if(tutorDetails.getUser() == null) tutorDetails.setUser(existingTutor.getUser()); // Repopulate User if needed
            model.addAttribute("tutor", tutorDetails);
            return "edit_tutor";
        }

        // Update logic
        existingTutor.setName(tutorDetails.getName());
        existingTutor.setSubject(tutorDetails.getSubject());
        if (existingTutor.getUser() != null && tutorDetails.getEmail() != null) {
            existingTutor.getUser().setEmail(tutorDetails.getEmail());
            userRepository.save(existingTutor.getUser());
        }
        tutorService.saveTutor(existingTutor);

        return "redirect:/tutors?updateSuccess";
    }

    @GetMapping("/tutors/delete/{id}")
    public String deleteTutor(@PathVariable Long id) {
        tutorService.deleteTutorById(id);
        return "redirect:/tutors?deleteSuccess";
    }
}