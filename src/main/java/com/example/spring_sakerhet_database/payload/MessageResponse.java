package com.example.spring_sakerhet_database.payload;

public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter and Setter (optional if using frameworks like Jackson)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

