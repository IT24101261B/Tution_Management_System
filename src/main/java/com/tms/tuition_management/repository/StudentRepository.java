package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; 

public interface StudentRepository extends JpaRepository<Student, Long> {

   
    Optional<Student> findByUserId(Long userId);

    Optional<Student> findByUser_Email(String email);
}
