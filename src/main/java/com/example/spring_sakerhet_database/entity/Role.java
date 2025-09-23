//package com.example.spring_sakerhet_database.entity;
//import jakarta.persistence.*;
//
//    @Entity
//    @Table(name = "roles")
//    public class Role {
//
//        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
//        @Column(name = "role_id")
//        private Long id;
//
//        private String name; // e.g. ROLE_USER, ROLE_ADMIN
//
//        // Getters and Setters
//        public Long getId() { return id; }
//        public void setId(Long id) { this.id = id; }
//
//        public String getName() { return name; }
//        public void setName(String name) { this.name = name; }
//    }

package com.example.spring_sakerhet_database.entity;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    // Constructors
    public Role() {}
    public Role(String name) {
        this.name = name;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}

