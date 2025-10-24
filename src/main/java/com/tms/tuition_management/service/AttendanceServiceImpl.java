package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Attendance;
import com.tms.tuition_management.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public void saveAll(List<Attendance> attendanceList) {
        attendanceRepository.saveAll(attendanceList);
    }

    @Override
    public List<Attendance> findByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    @Override
    public List<Attendance> findByScheduleId(Long scheduleId) {
        return attendanceRepository.findBySchedule_Id(scheduleId);
    }

    @Override
    @Transactional
    public void deleteByScheduleId(Long scheduleId) {
        attendanceRepository.deleteBySchedule_Id(scheduleId);
    }
}
