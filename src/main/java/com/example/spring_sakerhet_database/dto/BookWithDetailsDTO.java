package com.example.spring_sakerhet_database.dto;
import com.example.spring_sakerhet_database.entity.Author;

public class BookWithDetailsDTO {
    private Long id;
    private String title;
    private int publicationYear;
    private int availableCopies;
    private int totalCopies;
    private Author author;
   public BookWithDetailsDTO() {
   }

    public BookWithDetailsDTO(int availableCopies, Long id, String title, int publicationYear, int totalCopies, Author author) {
        this.availableCopies = availableCopies;
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.author = author;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }



    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
