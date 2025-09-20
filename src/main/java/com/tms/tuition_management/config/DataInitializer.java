package com.tms.tuition_management.config;

import com.tms.tuition_management.model.Role;
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.RoleRepository;
import com.tms.tuition_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_ADMIN"));
            roleRepository.save(new Role("ROLE_TUTOR"));
            roleRepository.save(new Role("ROLE_STUDENT"));
            roleRepository.save(new Role("ROLE_PARENT"));
        }

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@tms.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println(">>>>>>>>>>>> Created default admin user! <<<<<<<<<<<<");
        }
    }
}