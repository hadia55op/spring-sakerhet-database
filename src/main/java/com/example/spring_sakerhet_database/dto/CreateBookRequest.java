package com.example.spring_sakerhet_database.dto;



public class CreateBookRequest {
    private String title;
    private int publicationYear;
    private int availableCopies;
    private int totalCopies;
    private Long authorId;

    public CreateBookRequest(String title, int publicationYear, int availableCopies, int totalCopies, Long authorId) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.authorId = authorId;
    }

    public CreateBookRequest() {
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
}

