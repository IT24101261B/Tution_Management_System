package com.tms.tuition_management.service;

import com.tms.tuition_management.model.Parent;
import com.tms.tuition_management.model.Student;
import com.tms.tuition_management.model.Tutor;
import com.tms.tuition_management.model.User;
import java.util.List;

public interface UserService {
    void registerNewUser(String name, String email, String password, String role, String subject, String phone, String childEmail);
    void createAdminStudent(Student student, String password);
    void createTutorUser(Tutor tutor, String password);
    void createParentUser(Parent parent, String password, List<Long> studentIds);
    User findByEmail(String email);
    User findById(Long id);
}
