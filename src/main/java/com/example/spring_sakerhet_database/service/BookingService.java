package com.example.spring_sakerhet_database.service;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.core.userdetails.UserDetails;
@Service
public class BookingService {

    public void createBooking(UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("You do not have permission to create a booking.");
        }

        // proceed with booking creation
    }
}
