package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Tutor;
import java.util.List;

public interface TutorService {
    List<Tutor> getAllTutors();
    Tutor saveTutor(Tutor tutor);
    Tutor getTutorById(Long id);
    void deleteTutorById(Long id);
    Tutor findByUserId(Long userId);
}