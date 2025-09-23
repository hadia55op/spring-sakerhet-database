package com.example.spring_sakerhet_database.controller;

import com.example.spring_sakerhet_database.dto.BookWithDetailsDTO;
import com.example.spring_sakerhet_database.dto.CreateBookRequest;
import com.example.spring_sakerhet_database.entity.Book;
import com.example.spring_sakerhet_database.repository.BookRepository;
import com.example.spring_sakerhet_database.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/books")  // http://localhost:3030/books // Public endpoints (books listing) without token
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
// Public endpoints
    @GetMapping("/search")//http://localhost:3030/books/search?title=Doctor Glas
    public ResponseEntity<?> searchBooks(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String author) {
        List<BookWithDetailsDTO> results = bookService.searchBooks(title, author);
        return ResponseEntity.ok(results);
    }
//http://localhost:3030/books
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")  //  Only ADMIN can add books
    public BookWithDetailsDTO addBook(@RequestBody CreateBookRequest request) {
        return bookService.addBook(request);
    }


}

       // "title": "The Brothers Lionheart",
        //    "publicationYear": 1973,
         //   "availableCopies": 6,
          //  "totalCopies": 12,
         //   "authorId": 1
    //}





