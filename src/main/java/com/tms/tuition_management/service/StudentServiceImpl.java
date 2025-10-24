package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.User; 
import com.tms.tuition_management.repository.StudentRepository;
import com.tms.tuition_management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository; 

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository; 
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
    @Transactional 
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        Student student = getStudentById(id);
        if (student != null) {
            
            student.getSchedules().forEach(schedule -> schedule.getStudents().remove(student));
           
            student.getParents().forEach(parent -> parent.getStudents().remove(student));

            studentRepository.delete(student);

           
            if (student.getUser() != null) {
                userRepository.delete(student.getUser());
            }
        }
    }

   @Override
    public Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId).orElse(null);
        
    }
  
    @Override
    public long countStudents() {
        return studentRepository.count();
    }
}
