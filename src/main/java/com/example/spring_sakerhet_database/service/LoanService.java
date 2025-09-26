package com.example.spring_sakerhet_database.service;



import com.example.spring_sakerhet_database.entity.Book;
import com.example.spring_sakerhet_database.entity.Loan;
import com.example.spring_sakerhet_database.entity.User;
import com.example.spring_sakerhet_database.exception.ResourceNotFoundException;
import com.example.spring_sakerhet_database.repository.BookRepository;
import com.example.spring_sakerhet_database.repository.LoanRepository;
import com.example.spring_sakerhet_database.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository,
                       UserRepository userRepository,
                       BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;

    }

    //http://localhost:3030/users/1/loans requirea ADMIN token OR  user 1 must login and use the tokn to get loan data.
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findLoansByUserId(userId);
    }

}
