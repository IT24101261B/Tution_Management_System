package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Parent;
import java.util.List;

public interface ParentService {

    // --- Add these missing methods ---
    List<Parent> getAllParents();

    Parent getParentById(Long id);

    void saveParent(Parent parent); // Can be used for both create and update

    void deleteParentById(Long id);

    void updateParentStudents(Parent parent, List<Long> studentIds);
    // --- End of added methods ---

    Parent findByUserId(Long userId); // Existing method if you have it
}
