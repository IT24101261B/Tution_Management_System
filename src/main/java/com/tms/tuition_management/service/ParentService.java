package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Parent;
import java.util.List;

public interface ParentService {

    List<Parent> getAllParents();

    Parent getParentById(Long id);

    void saveParent(Parent parent); 

    void deleteParentById(Long id);

    void updateParentStudents(Parent parent, List<Long> studentIds);

    Parent findByUserId(Long userId); 
}
