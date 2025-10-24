package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; 

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByUserId(Long userId);
}
