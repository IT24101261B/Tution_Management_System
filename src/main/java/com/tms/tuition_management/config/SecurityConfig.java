package com.tms.tuition_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/", "/register", "/register/save", "/login", "/css/**").permitAll()
                                .requestMatchers("/admin/**", "/students/**", "/tutors/**", "/parents/**").hasRole("ADMIN")
                                .requestMatchers("/schedules/edit/**", "/schedules/update/**", "/schedules/delete/**").hasAnyRole("ADMIN", "TUTOR")
                                .requestMatchers("/lessons/download/**").authenticated()
                                .requestMatchers("/lessons/**", "/tutor/**", "/attendance/**").hasAnyRole("ADMIN", "TUTOR")

                                .requestMatchers("/portal/**", "/messages/**", "/chat/**").hasAnyRole("PARENT", "TUTOR")
                                .requestMatchers("/student/**").hasRole("STUDENT")
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/?logout")
                                .permitAll()
                );
        return http.build();
    }
}
