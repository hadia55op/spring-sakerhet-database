package com.example.spring_sakerhet_database.repository;
import org.springframework.data.jpa.repository.Query;


import com.example.spring_sakerhet_database.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(String name);
}
