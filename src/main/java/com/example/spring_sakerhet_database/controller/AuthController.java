package com.example.spring_sakerhet_database.controller;

import com.example.spring_sakerhet_database.payload.*;
import com.example.spring_sakerhet_database.utility.InputSanitizer;
import com.example.spring_sakerhet_database.utility.JwtUtils;
import com.example.spring_sakerhet_database.service.TokenBlacklistService;
import com.example.spring_sakerhet_database.entity.User;
import com.example.spring_sakerhet_database.repository.UserRepository;
import com.example.spring_sakerhet_database.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
//########################################
import com.example.spring_sakerhet_database.entity.Role;

import com.example.spring_sakerhet_database.repository.RoleRepository;

import jakarta.validation.Valid;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;




import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.AuthenticationException;


import java.time.LocalDate;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;
//User logindetails
//{
//        "email": "sara@example.com",
//        "password": "Test1000"}


@RestController
@RequestMapping("/api/auth")//admin login on //http://localhost:3030/api/auth/login then can use
                                         // {"email": "erik.eriksson@email.com",
                                        // "password" : "password456"}

public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    //remembers how many time every user tries to login

    private final Map<String, List<Long>> loginRateLimiting = new ConcurrentHashMap<>();

    private static final int AttemptsLimit = 5;
    private static final long LockoutDuration= 60_000;//one minute
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TokenBlacklistService blacklistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private InputSanitizer inputSanitizer;
    @PostMapping("/login")
    //med @Valid-annotationer vid misslyckade vlidering kommer Spring automatiskt att kasta MethodArgumentNotValidException
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

//Memory-based lockout logic:
        loginRateLimiting.compute(email, (key, attempts) -> {
            if (attempts == null) attempts = new ArrayList<>();
            long now = System.currentTimeMillis();
            attempts.removeIf(time -> time + LockoutDuration < now);
            return attempts;
        });

        // Check if blocked due to too many attempts
        if (loginRateLimiting.getOrDefault(email, new ArrayList<>()).size() >= AttemptsLimit) {
            logger.warn("Blocked login attempt for user {} due to too many failed attempts", email);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many failed login attempts. Please try again later.");
        }

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
            );

            // Successful login — clear attempts
            loginRateLimiting.remove(email);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String accessToken = jwtUtils.generateAccessToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);
            //Fetch user from database using email
            Optional<User> optionalUser = userRepo.findByEmail(email);
            if (!optionalUser.isPresent()) {
                logger.error("Authenticated user not found in database: {}", email);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User record not found.");
            }
            User user = optionalUser.get();

            logger.info("User {} logged in successfully", email);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("roles", userDetails.getAuthorities());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("email", user.getEmail());
            response.put("userId", user.getId());
            response.put("registrationDate", user.getRegistrationDate());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            // Failed login record attempt
            loginRateLimiting.compute(email, (key, attempts) -> {
                if (attempts == null) attempts = new ArrayList<>();
                attempts.add(System.currentTimeMillis());
                return attempts;
            });

            logger.warn("Failed login attempt for user {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, @RequestBody(required = false) TokenRefreshRequest tokenRequest) {
        // Extract Access Token from header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            if (jwtUtils.validateToken(accessToken)) {
                String accessJti = jwtUtils.getJtiFromToken(accessToken);
                blacklistService.blacklistJti(accessJti);
            }
        }

        // Handle Refresh Token from request body if present
        if (tokenRequest != null && tokenRequest.getRefreshToken() != null) {
            String refreshToken = tokenRequest.getRefreshToken();
            if (jwtUtils.validateToken(refreshToken)) {
                String refreshJti = jwtUtils.getJtiFromToken(refreshToken);
                blacklistService.blacklistJti(refreshJti);
            }
        }

        // Clear Spring Security Context
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(new MessageResponse("Logout successful"));
    }
    @PostMapping("/refresh")             // exampel{
                                               //   "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                        //}

    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
       //  Get user details from the token
        if (jwtUtils.validateToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Create Authentication object from UserDetails to pass into the JWT generator
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            // Generate new tokens
            String newAccessToken = jwtUtils.generateAccessToken(authentication);
            String newRefreshToken = jwtUtils.generateRefreshToken(authentication);

            return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }
    }








    //@PostMapping("/register")             //http://localhost:3030/api/auth/register
                                    //{
                                    //  "email": "sara@example.com",
                                     //  "password": "Test1000",
                                   //    "firstName": "Sara",
                                    //    "lastName": "Khan"
                                       // }
//    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request) {
//
//        // Check if email is already in use
//        if (userRepository.existsByEmail(request.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body("Email is already taken");
//        }
//
//        //  Create new User entity
//        User user = new User();
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRegistrationDate(LocalDate.now());
//
//        //  gives default role "ROLE_USER"
//        Role userRole = roleRepo.findByName("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));
//
//        user.setRoles(Set.of(userRole));
//
//        //  add user to DB
//        userRepository.save(user);
//
//        return ResponseEntity.ok("User registered successfully!");
//    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        // Sanitize inputs
        String email = inputSanitizer.sanitize(request.getEmail());
        String firstName = inputSanitizer.sanitize(request.getFirstName());
        String lastName = inputSanitizer.sanitize(request.getLastName());

        // Check if email is already in use
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        // Create new user
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRegistrationDate(LocalDate.now());

        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));

        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

}










