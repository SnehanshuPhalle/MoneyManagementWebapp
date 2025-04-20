package com.money.money_management.repository;

import com.money.money_management.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Find a budget by category, year, and month
    Optional<Budget> findByCategoryAndYearAndMonth(String category, int year, int month);

    // Find a default budget for a category (ignoring year and month)
    Optional<Budget> findByCategoryAndIsDefaultTrue(String category);

    // Optional: Find all budgets for a specific year and month
    List<Budget> findByYearAndMonth(int year, int month);

    // Existing method to find a budget by category
    Optional<Budget> findByCategory(String category);
}

