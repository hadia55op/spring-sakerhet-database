package com.example.spring_sakerhet_database.dto;



import java.time.LocalDate;

public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate registrationDate;
    public UserDto() {
        // Needed for manual mapping or frameworks like Jackson
    }


    public UserDto(Long id, String email, String firstName, String lastName, LocalDate registrationDate) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationDate = registrationDate;
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
}

