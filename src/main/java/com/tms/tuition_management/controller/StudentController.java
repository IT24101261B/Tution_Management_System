package com.tms.tuition_management.controller;

import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.UserRepository;
import com.tms.tuition_management.service.StudentService;
import com.tms.tuition_management.service.UserService;
import jakarta.validation.Valid; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; 
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
    public String saveStudent(@Valid @ModelAttribute("student") Student student, 
                              BindingResult bindingResult,
                              @RequestParam String password, Model model) {
       
        if (student.getEmail() != null && userService.findByEmail(student.getEmail()) != null) {
            bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
        }

       
        if (bindingResult.hasErrors()) {
            model.addAttribute("student", student); 
            return "create_student"; 
        }

        
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
                                @Valid @ModelAttribute("student") Student studentDetails, 
                                BindingResult bindingResult,
                                Model model) {

        Student existingStudent = studentService.getStudentById(id);

       
        if (existingStudent.getUser() != null && studentDetails.getEmail() != null &&
                !existingStudent.getUser().getEmail().equals(studentDetails.getEmail())) {
            User userWithNewEmail = userService.findByEmail(studentDetails.getEmail());
            if (userWithNewEmail != null && !userWithNewEmail.getId().equals(existingStudent.getUser().getId())) {
                bindingResult.rejectValue("email", "email.exists", "An account with this email already exists.");
            }
        }

        
        if (bindingResult.hasErrors()) {
            studentDetails.setId(id); 
            if (studentDetails.getUser() == null) studentDetails.setUser(existingStudent.getUser());
            model.addAttribute("student", studentDetails); 
            return "edit_student"; 
        }

       
        existingStudent.setName(studentDetails.getName());
        existingStudent.setPhone(studentDetails.getPhone());
        if (existingStudent.getUser() != null && studentDetails.getEmail() != null) {
            existingStudent.getUser().setEmail(studentDetails.getEmail());
            userRepository.save(existingStudent.getUser()); 
        }
        studentService.saveStudent(existingStudent); 

        return "redirect:/students?updateSuccess";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students?deleteSuccess";
    }
}

