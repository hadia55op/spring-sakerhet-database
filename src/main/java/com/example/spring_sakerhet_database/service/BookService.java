package com.example.spring_sakerhet_database.service;

import com.example.spring_sakerhet_database.dto.BookWithDetailsDTO;
import com.example.spring_sakerhet_database.dto.CreateBookRequest;
import com.example.spring_sakerhet_database.entity.Author;
import com.example.spring_sakerhet_database.entity.Book;
import com.example.spring_sakerhet_database.exception.ResourceNotFoundException;
import com.example.spring_sakerhet_database.repository.AuthorRepository;
import com.example.spring_sakerhet_database.repository.BookRepository;
import org.springframework.stereotype.Service;
import com.example.spring_sakerhet_database.utility.InputSanitizer;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final InputSanitizer inputSanitizer;
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, InputSanitizer inputSanitizer) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.inputSanitizer = inputSanitizer;
    }
    public List<BookWithDetailsDTO> searchBooks(String title, String author) {
        List<Book> books;

        title = inputSanitizer.sanitize(title);
        author = inputSanitizer.sanitize(author);

        if (title != null && !title.isBlank()) {
            books = bookRepository.findBookByExactTitle(title);
        } else if (author != null && !author.isBlank()) {
            books = bookRepository.findBooksByAuthorLastName(author);
        } else {
            books = List.of();
        }

        return books.stream()
                .map(this::toBookWithDetailsDTO)
                .collect(Collectors.toList());
    }



    @PreAuthorize("hasRole('ADMIN')")
    public BookWithDetailsDTO addBook(CreateBookRequest createBookRequest) {
        Long authorId = createBookRequest.getAuthorId();
        Author author = authorRepository.findById(authorId).orElseThrow(() ->
                new ResourceNotFoundException("Author with ID " + authorId + " not found"));

        Book book = new Book();
        book.setTitle(inputSanitizer.sanitize(createBookRequest.getTitle()));  // sanitize title
        book.setPublicationYear(createBookRequest.getPublicationYear());
        book.setAvailableCopies(createBookRequest.getAvailableCopies());
        book.setTotalCopies(createBookRequest.getTotalCopies());
        book.setAuthor(author);

        bookRepository.save(book);
        return toBookWithDetailsDTO(book);
    }




    private BookWithDetailsDTO toBookWithDetailsDTO(Book book) {
        BookWithDetailsDTO dto = new BookWithDetailsDTO();
        dto.setId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAuthor(book.getAuthor());
        return dto;
    }
}

