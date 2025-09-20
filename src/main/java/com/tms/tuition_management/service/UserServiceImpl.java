package com.tms.tuition_management.service;

import com.tms.tuition_management.model.*;
import com.tms.tuition_management.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final ParentRepository parentRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           StudentRepository studentRepository, TutorRepository tutorRepository, ParentRepository parentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
        this.parentRepository = parentRepository;
    }

    @Override
    @Transactional
    public void registerNewUser(String name, String email, String password, String role, String subject, String phone, String childEmail) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(role.equalsIgnoreCase("STUDENT"));

        String roleName = "ROLE_" + role.toUpperCase();
        Role userRole = roleRepository.findByName(roleName);
        if (userRole == null) {
            userRole = new Role(roleName);
            roleRepository.save(userRole);
        }
        user.setRoles(new HashSet<>(Set.of(userRole)));
        userRepository.save(user);

        switch (role.toUpperCase()) {
            case "STUDENT":
                Student student = new Student();
                student.setName(name);
                student.setPhone(phone);
                student.setUser(user);
                studentRepository.save(student);
                break;
            case "TUTOR":
                Tutor tutor = new Tutor();
                tutor.setName(name);
                tutor.setSubject(subject);
                tutor.setUser(user);
                tutorRepository.save(tutor);
                break;
            case "PARENT":
                Parent parent = new Parent();
                parent.setName(name);
                parent.setPhone(phone);
                parent.setUser(user);
                if (childEmail != null && !childEmail.isEmpty()) {
                    Student child = studentRepository.findByUser_Email(childEmail);
                    if (child != null) {
                        parent.setStudents(new HashSet<>(Set.of(child)));
                    }
                }
                parentRepository.save(parent);
                break;
        }
    }

    @Override
    @Transactional
    public void createAdminStudent(Student student, String password) {
        User user = new User();
        user.setEmail(student.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        Role studentRole = roleRepository.findByName("ROLE_STUDENT");
        user.setRoles(new HashSet<>(Set.of(studentRole)));
        userRepository.save(user);
        student.setUser(user);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void createTutorUser(Tutor tutor, String password) {
        User user = new User();
        user.setEmail(tutor.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        Role tutorRole = roleRepository.findByName("ROLE_TUTOR");
        user.setRoles(new HashSet<>(Set.of(tutorRole)));
        userRepository.save(user);
        tutor.setUser(user);
        tutorRepository.save(tutor);
    }

    @Override
    @Transactional
    public void createParentUser(Parent parent, String password, List<Long> studentIds) {
        User user = new User();
        user.setEmail(parent.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        Role parentRole = roleRepository.findByName("ROLE_PARENT");
        user.setRoles(new HashSet<>(Set.of(parentRole)));
        userRepository.save(user);
        Set<Student> linkedStudents = studentRepository.findAllById(studentIds).stream().collect(Collectors.toSet());
        parent.setStudents(linkedStudents);
        parent.setUser(user);
        parentRepository.save(parent);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}