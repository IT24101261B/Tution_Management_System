package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Attendance;
import com.tms.tuition_management.model.Schedule;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.repository.AttendanceRepository;
import com.tms.tuition_management.repository.ScheduleRepository;
import com.tms.tuition_management.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;

    public StudentServiceImpl(StudentRepository studentRepository, AttendanceRepository attendanceRepository, ScheduleRepository scheduleRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.scheduleRepository = scheduleRepository;
    }
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    @Override
    public Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }
    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        studentRepository.findById(id).ifPresent(student -> {
            List<Attendance> attendances = attendanceRepository.findByStudentId(id);
            if (attendances != null && !attendances.isEmpty()) {
                attendanceRepository.deleteAll(attendances);
            }
            List<Schedule> schedules = scheduleRepository.findByStudents_Id(id);
            for (Schedule schedule : schedules) {
                schedule.getStudents().remove(student);
                scheduleRepository.save(schedule);
            }
            studentRepository.delete(student);
        });
    }
}
