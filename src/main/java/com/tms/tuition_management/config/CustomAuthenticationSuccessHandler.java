package com.tms.tuition_management.config;

import com.tms.tuition_management.model.Parent; 
import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.ParentRepository; 
import com.tms.tuition_management.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional; 

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository; 

    
    public CustomAuthenticationSuccessHandler(UserRepository userRepository, ParentRepository parentRepository) {
        this.userRepository = userRepository;
        this.parentRepository = parentRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = determineTargetUrl(authentication);
        response.sendRedirect(redirectUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "/login?error"; 
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            switch (authority.getAuthority()) {
                case "ROLE_ADMIN":
                    return "/admin/dashboard";
                case "ROLE_TUTOR":
                    return "/tutor/dashboard";
                case "ROLE_STUDENT":
                    return "/student/dashboard";
                case "ROLE_PARENT":
                    
                    Optional<Parent> parentOptional = parentRepository.findByUserId(user.getId());
                    if (parentOptional.isPresent()) {
                        
                        return "/portal"; 
                    } else {
                        
                        return "/login?error=parent_profile_missing";
                    }
            }
        }
        return "/login?error"; // Fallback
    }
}
