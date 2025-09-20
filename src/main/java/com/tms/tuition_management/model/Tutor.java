package com.tms.tuition_management.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tutors")
public class Tutor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String subject;
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Lesson> lessons;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) @JoinColumn(name = "user_id", referencedColumnName = "id") private User user;

    @Transient
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getEmail() { return this.user != null ? this.user.getEmail() : this.email; }
    public void setEmail(String email) { this.email = email; }
}