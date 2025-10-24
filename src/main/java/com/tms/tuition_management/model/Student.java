package com.tms.tuition_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Student name cannot be empty.")
    @Pattern(regexp = "^[A-Z][a-zA-Z\\s]*$", message = "Name must start with a capital letter and contain only letters and spaces.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "Phone number cannot be empty.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL) 
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; 
    @ManyToMany(mappedBy = "students")
    private Set<Schedule> schedules = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    private Set<Parent> parents = new HashSet<>();

    
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Set<Parent> getParents() {
        return parents;
    }

    public void setParents(Set<Parent> parents) {
        this.parents = parents;
    }

    
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
