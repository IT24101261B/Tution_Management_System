package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserId(Long userId);
    Student findByUser_Email(String email);
}
