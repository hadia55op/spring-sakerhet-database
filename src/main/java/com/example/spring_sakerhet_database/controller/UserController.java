package com.example.spring_sakerhet_database.controller;

import com.example.spring_sakerhet_database.entity.User;
import com.example.spring_sakerhet_database.entity.Role;
import com.example.spring_sakerhet_database.dto.UserDto;

import com.example.spring_sakerhet_database.repository.RoleRepository;
import com.example.spring_sakerhet_database.service.UserService;
import com.example.spring_sakerhet_database.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
 //First need admin login on //http://localhost:3030/api/auth/login then can use
//       {"email": "erik.eriksson@email.com",
//                            "password" : "password456"}

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;



//    @PostMapping
//    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
//        UserDto createdUser = userService.createUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//    }




    @PreAuthorize("#email == authentication.name or hasRole('ADMIN','USER')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable String email,
            Authentication authentication) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRegistrationDate()
        ));
    }




}

//http://localhost:3030/api/auth/register
//{
      //  "email": "sara@example.com",
      //  "password": "Test1000",
    //    "firstName": "Sara",
    //    "lastName": "Khan"
       // }
//http://localhost:3030/api/auth/login
//{
//        "email": "sara@example.com",
//        "password": "Test1000"}
//http://localhost:3030/api/users/email/sara@example.com
//http://localhost:3030/api/auth/register
 // "email": "sofia@example.com",
         // "password": "password333",
          //"firstName": "sofia",
          //"lastName": "olsson"
          //}
/*{
        "email": "mustafa@example.com",
        "password": "password222",
        "firstName": "mustafa",
        "lastName": "kamal"
        }*/