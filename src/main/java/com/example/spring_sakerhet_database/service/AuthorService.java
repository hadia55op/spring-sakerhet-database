package com.example.spring_sakerhet_database.service;
import com.example.spring_sakerhet_database.entity.Author;
import com.example.spring_sakerhet_database.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    public List<Author> getAuthorsByLastName(String lastName) {
        return authorRepository.findByLastName(lastName);
    }
// protected end point
    @PreAuthorize("hasRole('ADMIN')")
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }
}
