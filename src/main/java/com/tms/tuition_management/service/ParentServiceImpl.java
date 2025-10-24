package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Parent;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.repository.ParentRepository;
import com.tms.tuition_management.repository.StudentRepository; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository; 

    public ParentServiceImpl(ParentRepository parentRepository, StudentRepository studentRepository) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository; 
    }


    @Override
    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    @Override
    public Parent getParentById(Long id) {
        return parentRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional 
    public void saveParent(Parent parent) {
        parentRepository.save(parent);
    }

    @Override
    @Transactional
    public void deleteParentById(Long id) {
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
    }




    @Override
    public Parent findByUserId(Long userId) {
        return parentRepository.findByUserId(userId).orElse(null);
    }


}
