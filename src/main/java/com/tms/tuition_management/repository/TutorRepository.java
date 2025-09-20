package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Tutor findByUserId(Long userId);
}
