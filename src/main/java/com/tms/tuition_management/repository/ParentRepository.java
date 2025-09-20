package com.tms.tuition_management.repository;
import com.tms.tuition_management.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Parent findByUserId(Long userId);
}