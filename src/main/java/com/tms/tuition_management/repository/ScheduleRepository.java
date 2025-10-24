package com.tms.tuition_management.repository;

import com.tms.tuition_management.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByStudents_Id(Long studentId);
    List<Schedule> findByTutorId(Long tutorId);
}
