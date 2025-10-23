package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.model.User; // Ensure User is imported
import com.tms.tuition_management.repository.TutorRepository;
import com.tms.tuition_management.repository.UserRepository; // Import UserRepository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.util.List;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository; // Inject UserRepository

    public TutorServiceImpl(TutorRepository tutorRepository, UserRepository userRepository) {
        this.tutorRepository = tutorRepository;
        this.userRepository = userRepository; // Initialize UserRepository
    }

    @Override
    public List<Tutor> getAllTutors() {
        return tutorRepository.findAll();
    }

    @Override
    public Tutor getTutorById(Long id) {
        return tutorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional // Good practice for save/update
    public void saveTutor(Tutor tutor) {
        tutorRepository.save(tutor);
    }

    @Override
    @Transactional
    public void deleteTutorById(Long id) {
        Tutor tutor = getTutorById(id);
        if (tutor != null) {
            // Important: Handle relationships before deleting.
            // For example, unassign tutor from schedules or handle lessons.
            // For simplicity here, we'll just delete the tutor and user.
            // In a real app, you'd need more logic (e.g., set schedule.tutor to null).
            tutorRepository.delete(tutor);

            // Also delete the associated User account
            if (tutor.getUser() != null) {
                userRepository.delete(tutor.getUser());
            }
        }
    }

    @Override
    public Tutor findByUserId(Long userId) {
        return tutorRepository.findByUserId(userId).orElse(null);
        // Note: You need `Optional<Tutor> findByUserId(Long userId);` in TutorRepository
    }

    // --- Implement the missing method ---
    @Override
    public long countTutors() {
        return tutorRepository.count();
    }
}