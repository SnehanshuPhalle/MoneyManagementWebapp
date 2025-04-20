package com.money.money_management.repository;

import com.money.money_management.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    // Get all incomes for a specific user
    List<Income> findByUserId(Long userId);

    // Get all incomes by source (e.g., "SALARY", "FREELANCE", etc.)
    List<Income> findBySource(String source);

    // Get all incomes by a specific month and year for a user
    @Query("SELECT i FROM Income i WHERE i.user.id = :userId AND FUNCTION('YEAR', i.date) = :year AND FUNCTION('MONTH', i.date) = :month")
    List<Income> findByUserAndMonthYear(Long userId, int month, int year);

    // Get total income by user and year
    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId AND FUNCTION('YEAR', i.date) = :year")
    Double getTotalIncomeByUserAndYear(Long userId, int year);

    // Get all incomes within a date range
    List<Income> findByDateBetween(LocalDate startDate, LocalDate endDate);
}