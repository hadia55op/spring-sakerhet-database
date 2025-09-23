package com.example.spring_sakerhet_database.controller;

import com.example.spring_sakerhet_database.entity.Author;
import com.example.spring_sakerhet_database.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    //http://localhost:3030/authors public_endpoint
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    //  GET /authors/name/{lastName}
    //http://localhost:3030/authors/name/Lindgren  public-endpoint
     // public_endpoint
    @GetMapping("/name/{lastName}")
    public List<Author> getAuthorsByLastName(@PathVariable String lastName) {
        return authorService.getAuthorsByLastName(lastName);
    }

    //  POST /authors - ADMIN only
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorService.createAuthor(author);
    }
}

//{
//  "firstName": "TestAuthor",
//  "lastName": "Test",
//  "birthYear": 1907,
//  "nationality": "Test"
//}