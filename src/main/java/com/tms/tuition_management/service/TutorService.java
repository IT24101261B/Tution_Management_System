package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Tutor;
import java.util.List;

public interface TutorService {

    List<Tutor> getAllTutors();

    Tutor getTutorById(Long id);

    void saveTutor(Tutor tutor); // Used for both create & update

    void deleteTutorById(Long id);

    Tutor findByUserId(Long userId);

    // --- Add this missing method ---
    long countTutors();
}