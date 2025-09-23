package com.example.spring_sakerhet_database.dto;

public class JwtResponse {
    private String token;
    public JwtResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}
