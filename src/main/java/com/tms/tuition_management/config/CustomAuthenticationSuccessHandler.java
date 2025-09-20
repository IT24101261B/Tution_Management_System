package com.tms.tuition_management.config;

import com.tms.tuition_management.model.User;
import com.tms.tuition_management.repository.ParentRepository;
import com.tms.tuition_management.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final ParentRepository parentRepository;

    public CustomAuthenticationSuccessHandler(UserService userService, ParentRepository parentRepository) {
        this.userService = userService;
        this.parentRepository = parentRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = "/";
        User loggedInUser = userService.findByEmail(authentication.getName());

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/dashboard";
                break;
            } else if (role.equals("ROLE_TUTOR")) {
                redirectUrl = "/tutor/dashboard";
                break;
            } else if (role.equals("ROLE_PARENT")) {
                Long parentId = parentRepository.findByUserId(loggedInUser.getId()).getId();
                redirectUrl = "/portal/parent/" + parentId;
                break;
            } else if (role.equals("ROLE_STUDENT")) {
                redirectUrl = "/student/dashboard";
                break;
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
