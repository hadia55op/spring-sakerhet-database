package com.example.spring_sakerhet_database.repository;


import com.example.spring_sakerhet_database.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT * FROM loans WHERE user_id = :userId", nativeQuery = true)
    List<Loan> findLoansByUserId(@Param("userId") Long userId);

}

