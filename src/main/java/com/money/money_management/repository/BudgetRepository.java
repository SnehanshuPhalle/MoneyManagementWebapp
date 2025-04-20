package com.money.money_management.repository;

import com.money.money_management.model.Budget;
import com.money.money_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Find a budget by category, year, month, and user
    Optional<Budget> findByCategoryAndYearAndMonthAndUser(String category, int year, int month, User user);

    // Find a default budget for a category and user (ignoring year and month)
    Optional<Budget> findByCategoryAndIsDefaultTrueAndUser(String category, User user);

    // Find all budgets for a specific user and month/year
    List<Budget> findByYearAndMonthAndUser(int year, int month, User user);

    // Find a budget by category for a specific user
    Optional<Budget> findByCategoryAndUser(String category, User user);

    // Find a budget by ID and user
    Optional<Budget> findByIdAndUser(Long id, User user);

    // Find all budgets for a user (without month/year filtering)
    List<Budget> findByUser(User user);
}
