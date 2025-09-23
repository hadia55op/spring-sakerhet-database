package com.example.spring_sakerhet_database.controller;


import com.example.spring_sakerhet_database.entity.Loan;

import com.example.spring_sakerhet_database.service.LoanService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.spring_sakerhet_database.entity.User;
import com.example.spring_sakerhet_database.repository.UserRepository;

import org.springframework.security.core.Authentication;


import java.util.List;


@RestController
@RequestMapping("/users")
public class LoanController {

    private final LoanService loanService;
    private final UserRepository userRepository;

    @Autowired
    public LoanController(LoanService loanService, UserRepository userRepository) {
        this.loanService = loanService;
        this.userRepository = userRepository;
    }
    //http://localhost:3030/users/1/loans
    @GetMapping("/{userId}/loans")
    public ResponseEntity<?> getLoansByUser(
            @PathVariable Long userId,
            Authentication authentication) {

        // Debug info
        System.out.println("Requested userId: " + userId);
        System.out.println("authentication.getName(): " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        // Find the logged in user
        User loggedUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()) || "ADMIN".equals(r.getName()));

        if (!loggedUser.getId().equals(userId) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        List<Loan> loans = loanService.getLoansByUserId(userId);
        return ResponseEntity.ok(loans);


    }
}