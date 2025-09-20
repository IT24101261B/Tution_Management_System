package com.tms.tuition_management.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String phone;
    @ManyToMany(mappedBy = "students") private Set<Parent> parents;
    @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name = "user_id", referencedColumnName = "id") private User user;

    @Transient
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Set<Parent> getParents() { return parents; }
    public void setParents(Set<Parent> parents) { this.parents = parents; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getEmail() { return this.user != null ? this.user.getEmail() : this.email; }
    public void setEmail(String email) { this.email = email; }
}