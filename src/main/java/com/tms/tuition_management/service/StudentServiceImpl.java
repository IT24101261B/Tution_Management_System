package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.User; // Ensure User is imported
import com.tms.tuition_management.repository.StudentRepository;
import com.tms.tuition_management.repository.UserRepository; // Import UserRepository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository; // Inject UserRepository

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository; // Initialize UserRepository
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional // Good practice for save/update
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        Student student = getStudentById(id);
        if (student != null) {
            // Optional: Clean up relationships if needed
            // Remove student from schedules
            student.getSchedules().forEach(schedule -> schedule.getStudents().remove(student));
            // Remove student from parents
            student.getParents().forEach(parent -> parent.getStudents().remove(student));

            studentRepository.delete(student);

            // Also delete the associated User account
            if (student.getUser() != null) {
                userRepository.delete(student.getUser());
            }
        }
    }


    @Override
    public Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId).orElse(null);
        // Note: You need `Optional<Student> findByUserId(Long userId);` in StudentRepository
    }

    // --- Implement the missing method ---
    @Override
    public long countStudents() {
        return studentRepository.count();
    }
}
