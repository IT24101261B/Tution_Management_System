package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Ensure Optional is imported

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Ensure this method returns Optional<Student>
    Optional<Student> findByUserId(Long userId);

    // Add this if it's missing (needed for Parent linking)
    Optional<Student> findByUser_Email(String email);
}
