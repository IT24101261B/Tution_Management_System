package com.tms.tuition_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tutors")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Tutor name cannot be empty.")
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]*$", message = "Name must start with a capital letter and contain only letters and spaces.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "Subject cannot be empty.")
    @Size(min = 2, max = 100, message = "Subject must be between 2 and 100 characters.")
    private String subject;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Contains the email field, validation is handled in User class

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Schedule> schedules = new HashSet<>();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();

    // --- Standard Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    // Helper method to get email for forms
    @Transient
    public String getEmail() {
        return (this.user != null) ? this.user.getEmail() : null;
    }

    public void setEmail(String email) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setEmail(email);
    }
}