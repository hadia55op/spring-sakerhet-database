package com.example.spring_sakerhet_database.service;
import com.example.spring_sakerhet_database.entity.Author;
import com.example.spring_sakerhet_database.repository.AuthorRepository;
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

    @PreAuthorize("hasRole('ADMIN')")
    public Author createAuthor(Author author) {
        // Validate author fields
        if (author.getFirstName() == null || author.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (author.getLastName() == null || author.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        // Sanitize input
        author.setFirstName(sanitize(author.getFirstName()));
        author.setLastName(sanitize(author.getLastName()));



        return authorRepository.save(author);
    }

    // remove HTML tags
    private String sanitize(String input) {
        return input.replaceAll("<[^>]*>", "");  // removes HTML tags
    }
}
