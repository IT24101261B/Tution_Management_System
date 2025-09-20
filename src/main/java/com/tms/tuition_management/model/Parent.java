package com.tms.tuition_management.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "parents")
public class Parent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String phone;
    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(name = "parent_student", joinColumns = @JoinColumn(name = "parent_id"), inverseJoinColumns = @JoinColumn(name = "student_id")) private Set<Student> students;
    @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name = "user_id", referencedColumnName = "id") private User user;

    @Transient
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Set<Student> getStudents() { return students; }
    public void setStudents(Set<Student> students) { this.students = students; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getEmail() { return this.user != null ? this.user.getEmail() : this.email; }
    public void setEmail(String email) { this.email = email; }
}