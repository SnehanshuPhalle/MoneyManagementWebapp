package com.money.money_management.repository;

import com.money.money_management.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Get all expenses by user ID
    List<Expense> findByUserId(Long userId);

    // Get expenses by category and specific year and month
    @Query("SELECT e FROM Expense e WHERE e.category = ?1 AND YEAR(e.date) = ?2 AND MONTH(e.date) = ?3")
    List<Expense> findByCategoryAndYearAndMonth(String category, int year, int month);

    // Get all expenses for a specific user within a date range (monthly/yearly)
    @Query("SELECT e FROM Expense e WHERE e.user.id = ?1 AND e.date BETWEEN ?2 AND ?3")
    List<Expense> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    // Get the total amount for a category in a specific month and year
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.category = ?1 AND YEAR(e.date) = ?2 AND MONTH(e.date) = ?3")
    Double getCategoryTotalByMonthAndYear(String category, int year, int month);

    // Get the total amount for a specific user in a specific month and year
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = ?1 AND YEAR(e.date) = ?2 AND MONTH(e.date) = ?3")
    Double getUserTotalByMonthAndYear(Long userId, int year, int month);
}
