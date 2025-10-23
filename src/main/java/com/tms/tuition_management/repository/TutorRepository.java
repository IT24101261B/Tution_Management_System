package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Ensure Optional is imported

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    // Ensure this method returns Optional<Tutor>
    Optional<Tutor> findByUserId(Long userId);
}

