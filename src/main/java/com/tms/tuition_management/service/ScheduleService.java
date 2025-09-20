package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Schedule;
import java.util.List;

public interface ScheduleService {
    List<Schedule> getAllSchedules();
    Schedule saveSchedule(Schedule schedule);
    Schedule getScheduleById(Long id);
    void deleteScheduleById(Long id);
    List<Schedule> findSchedulesByStudentId(Long studentId);
    List<Schedule> findSchedulesByTutorId(Long tutorId);
}