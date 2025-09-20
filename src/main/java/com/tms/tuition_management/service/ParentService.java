package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Parent;
import java.util.List;

public interface ParentService {
    Parent save(Parent parent);
    List<Parent> findAll();
    Parent findById(Long id);
    void deleteById(Long id);
}