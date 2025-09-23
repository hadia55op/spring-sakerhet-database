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

/*
    // 🔒 Only USER or ADMIN can create a loan
    // return/extend only if loan belongs to user or is admin
    @PreAuthorize("#authEmail == authentication.name or hasRole('ADMIN')")
    @Transactional
    public Loan createLoan(Long userId, Long bookId, String authEmail) {
        User loggedUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()) || "ADMIN".equals(r.getName()));

        if (!loggedUser.getId().equals(userId) && !isAdmin) {
            throw new SecurityException("Access denied");
        }

        User userToLoan = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookId + " not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of book ID " + bookId);
        }

        // decrease copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setUser(userToLoan);
        loan.setBook(book);
        loan.setBorrowedDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));

        return loanRepository.save(loan);
    }

    // 🔒 Only USER or ADMIN can return books
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PreAuthorize("hasRole('ADMIN') or @loanSecurity.isLoanOwnedBy(#id, authentication.name)")
    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with ID " + loanId + " not found"));

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException("Book has already been returned.");
        }

        loan.setReturnedDate(LocalDate.now());

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    // 🔒 Only USER or ADMIN can extend loans
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PreAuthorize("hasRole('ADMIN') or @loanSecurity.isLoanOwnedBy(#id, authentication.name)")

    public Loan extendLoan(Long loanId, String authEmail) {
        User loggedUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()) || "ADMIN".equals(r.getName()));

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with ID " + loanId + " not found"));

        if (!loan.getUser().getId().equals(loggedUser.getId()) && !isAdmin) {
            throw new SecurityException("Access denied");
        }

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException("Loan already returned; cannot extend");
        }

        loan.setDueDate(loan.getDueDate().plusWeeks(1));
        return loanRepository.save(loan);
    }
*/
}

