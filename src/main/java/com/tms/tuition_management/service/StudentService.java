package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Student;
import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    void saveStudent(Student student); 

    void deleteStudentById(Long id);

    Student findByUserId(Long userId);

    
    long countStudents();
}
