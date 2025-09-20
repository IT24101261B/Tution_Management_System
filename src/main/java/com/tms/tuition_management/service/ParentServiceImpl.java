package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Parent;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.repository.ParentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;

    public ParentServiceImpl(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    @Override
    public Parent save(Parent parent) {
        return parentRepository.save(parent);
    }

    @Override
    public List<Parent> findAll() {
        return parentRepository.findAll();
    }

    @Override
    public Parent findById(Long id) {
        return parentRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        parentRepository.findById(id).ifPresent(parent -> {
            for (Student student : parent.getStudents()) {
                student.getParents().remove(parent);
            }
            parentRepository.delete(parent);
        });
    }
}