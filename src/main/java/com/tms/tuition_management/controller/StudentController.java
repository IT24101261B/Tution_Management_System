package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final UserRepository userRepository;
    private final UserService userService; // FIX: Declare the field

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
    public String saveStudent(@ModelAttribute("student") Student student, @RequestParam String password) {
        // This line will now work correctly
        userService.createAdminStudent(student, password);
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "edit_student";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") Student studentDetails) {
        Student existingStudent = studentService.getStudentById(id);
        existingStudent.setName(studentDetails.getName());
        existingStudent.setPhone(studentDetails.getPhone());

        User user = existingStudent.getUser();
        if (user != null) {
            user.setEmail(studentDetails.getEmail());
            userRepository.save(user);
        }

        studentService.saveStudent(existingStudent);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}