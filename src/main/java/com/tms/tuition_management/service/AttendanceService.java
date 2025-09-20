package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Attendance;
import java.util.List;

public interface AttendanceService {
    void saveAll(List<Attendance> attendanceList);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByScheduleId(Long scheduleId);
    void deleteByScheduleId(Long scheduleId);
}