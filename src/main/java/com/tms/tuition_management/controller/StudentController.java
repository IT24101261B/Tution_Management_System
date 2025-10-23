package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.UserService;
import jakarta.validation.Valid; // Add this import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Add this import
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final UserRepository userRepository;
    private final UserService userService;

    public StudentController(StudentService studentService, UserRepository userRepository, UserService userService) {
        this.studentService = studentService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "create_student";
    }

    @PostMapping("/students")
    public String saveStudent(@Valid @ModelAttribute("student") Student student, // Add @Valid
                              BindingResult bindingResult, // Add BindingResult
                              @RequestParam String password, Model model) {
        // Check if email already exists before other validation
        if (student.getEmail() != null && userService.findByEmail(student.getEmail()) != null) {
            bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
        }

        // Check for validation errors from annotations OR custom checks
        if (bindingResult.hasErrors()) {
            model.addAttribute("student", student); // Send back the object to keep form data
            return "create_student"; // Return to the form
        }

        // Proceed with creating the student if no errors
        userService.createAdminStudent(student, password);
        return "redirect:/students?success";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "edit_student";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student studentDetails, // Add @Valid
                                BindingResult bindingResult, // Add BindingResult
                                Model model) {

        Student existingStudent = studentService.getStudentById(id);

        // Check if email is being changed and if the new email already exists for *another* user
        if (existingStudent.getUser() != null && studentDetails.getEmail() != null &&
                !existingStudent.getUser().getEmail().equals(studentDetails.getEmail())) {
            User userWithNewEmail = userService.findByEmail(studentDetails.getEmail());
            if (userWithNewEmail != null && !userWithNewEmail.getId().equals(existingStudent.getUser().getId())) {
                bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
            }
        }

        // Check for validation errors from annotations OR custom checks
        if (bindingResult.hasErrors()) {
            studentDetails.setId(id); // Keep the ID for the form action URL
            if (studentDetails.getUser() == null) studentDetails.setUser(existingStudent.getUser()); // Repopulate User if needed
            model.addAttribute("student", studentDetails); // Send back the object with errors
            return "edit_student"; // Return to the edit form
        }

        // Update logic if no errors
        existingStudent.setName(studentDetails.getName());
        existingStudent.setPhone(studentDetails.getPhone());
        if (existingStudent.getUser() != null && studentDetails.getEmail() != null) {
            existingStudent.getUser().setEmail(studentDetails.getEmail());
            userRepository.save(existingStudent.getUser()); // Save the updated User
        }
        studentService.saveStudent(existingStudent); // Save the updated Student

        return "redirect:/students?updateSuccess";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students?deleteSuccess";
    }
}

