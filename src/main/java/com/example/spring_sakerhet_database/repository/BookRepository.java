package com.example.spring_sakerhet_database.repository;

import com.example.spring_sakerhet_database.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
   @Query(value = "SELECT * FROM books WHERE LOWER(title) = LOWER(:title)", nativeQuery = true)
   List<Book> findBookByExactTitle(@Param("title") String title);



    @Query("SELECT b FROM Book b WHERE LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Book> findBooksByAuthorLastName(@Param("lastName") String lastName);

}


