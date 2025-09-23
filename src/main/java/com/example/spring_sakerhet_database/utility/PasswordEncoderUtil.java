package com.example.spring_sakerhet_database.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        String rawPassword = "password456";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("Encoded password: " + encoded);
    }
}