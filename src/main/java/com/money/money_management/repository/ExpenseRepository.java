package com.money.money_management.repository;

import com.money.money_management.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // You can later add custom queries here if needed
}
