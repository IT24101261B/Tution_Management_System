package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Student;
import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    void saveStudent(Student student); // Used for both create & update

    void deleteStudentById(Long id);

    Student findByUserId(Long userId);

    // --- Add this missing method ---
    long countStudents();
}