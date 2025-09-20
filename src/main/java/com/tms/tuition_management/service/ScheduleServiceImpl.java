package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Schedule;
import com.tms.tuition_management.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }
    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteScheduleById(Long id) {
        scheduleRepository.deleteById(id);
    }
    @Override
    public List<Schedule> findSchedulesByStudentId(Long studentId) {
        return scheduleRepository.findByStudents_Id(studentId);
    }
    @Override
    public List<Schedule> findSchedulesByTutorId(Long tutorId) {
        return scheduleRepository.findByTutorId(tutorId);
    }
}