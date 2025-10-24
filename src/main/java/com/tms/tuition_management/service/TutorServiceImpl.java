package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.model.User; 
import com.tms.tuition_management.repository.TutorRepository;
import com.tms.tuition_management.repository.UserRepository; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.util.List;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository; 

    public TutorServiceImpl(TutorRepository tutorRepository, UserRepository userRepository) {
        this.tutorRepository = tutorRepository;
        this.userRepository = userRepository; 
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
    @Transactional 
    public void saveTutor(Tutor tutor) {
        tutorRepository.save(tutor);
    }

    @Override
    @Transactional
    public void deleteTutorById(Long id) {
        Tutor tutor = getTutorById(id);
        if (tutor != null) {
            tutorRepository.delete(tutor);

            if (tutor.getUser() != null) {
                userRepository.delete(tutor.getUser());
            }
        }
    }

    @Override
    public Tutor findByUserId(Long userId) {
        return tutorRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public long countTutors() {
        return tutorRepository.count();
    }
}
