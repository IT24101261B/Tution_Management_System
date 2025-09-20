package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Lesson;
import com.tms.tuition_management.model.Schedule;
import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.repository.LessonRepository;
import com.tms.tuition_management.repository.ScheduleRepository;
import com.tms.tuition_management.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final LessonRepository lessonRepository;
    private final ScheduleRepository scheduleRepository;

    public TutorServiceImpl(TutorRepository tutorRepository, LessonRepository lessonRepository, ScheduleRepository scheduleRepository) {
        this.tutorRepository = tutorRepository;
        this.lessonRepository = lessonRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Tutor> getAllTutors() {
        return tutorRepository.findAll();
    }
    @Override
    public Tutor saveTutor(Tutor tutor) {
        return tutorRepository.save(tutor);
    }
    @Override
    public Tutor getTutorById(Long id) {
        return tutorRepository.findById(id).orElse(null);
    }
    @Override
    public Tutor findByUserId(Long userId) {
        return tutorRepository.findByUserId(userId);
    }
    @Override
    @Transactional
    public void deleteTutorById(Long id) {
        tutorRepository.findById(id).ifPresent(tutor -> {
            List<Lesson> lessons = lessonRepository.findByTutorId(id);
            if (lessons != null && !lessons.isEmpty()) {
                lessonRepository.deleteAll(lessons);
            }
            List<Schedule> schedules = scheduleRepository.findByTutorId(id);
            for (Schedule schedule : schedules) {
                schedule.setTutor(null);
                scheduleRepository.save(schedule);
            }
            tutorRepository.delete(tutor);
        });
    }
}