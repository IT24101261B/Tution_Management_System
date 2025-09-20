package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findBySchedule_Id(Long scheduleId);
    void deleteBySchedule_Id(Long scheduleId);
}