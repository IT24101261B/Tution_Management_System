package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Parent;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.repository.ParentRepository;
import com.tms.tuition_management.repository.StudentRepository; // Import StudentRepository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional; // Import Optional
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository; // Inject StudentRepository

    public ParentServiceImpl(ParentRepository parentRepository, StudentRepository studentRepository) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository; // Initialize StudentRepository
    }

    // --- Implement the missing methods ---

    @Override
    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    @Override
    public Parent getParentById(Long id) {
        return parentRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional // Good practice for save/update/delete operations
    public void saveParent(Parent parent) {
        parentRepository.save(parent);
    }

    @Override
    @Transactional
    public void deleteParentById(Long id) {
        // Optional: Add logic here if you need to clean up relationships before deleting
        parentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateParentStudents(Parent parent, List<Long> studentIds) {
        Set<Student> linkedStudents = new HashSet<>();
        if (studentIds != null && !studentIds.isEmpty()) {
            linkedStudents = studentRepository.findAllById(studentIds).stream().collect(Collectors.toSet());
        }
        parent.setStudents(linkedStudents);
        // The saveParent call in the controller will persist this change
    }

    // --- End of implemented methods ---


    @Override
    public Parent findByUserId(Long userId) {
        // Note: Ensure `Optional<Parent> findByUserId(Long userId);` exists in ParentRepository
        return parentRepository.findByUserId(userId).orElse(null);
    }

    // Removed the incorrect ParentRepository example code from here

}