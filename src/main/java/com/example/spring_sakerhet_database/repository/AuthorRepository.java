package com.example.spring_sakerhet_database.repository;


import com.example.spring_sakerhet_database.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {


    @Query("SELECT a FROM Author a WHERE LOWER(a.lastName) = LOWER(:lastName)")
    List<Author> findByLastName(@Param("lastName") String lastName);

}

