package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student saveStudent(Student student);
    Student getStudentById(Long id);
    void deleteStudentById(Long id);
    Student findByUserId(Long userId);
}