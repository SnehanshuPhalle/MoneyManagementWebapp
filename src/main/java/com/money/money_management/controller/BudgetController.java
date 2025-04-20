package com.money.money_management.controller;


import com.money.money_management.model.Budget;
import com.money.money_management.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // Endpoint to set or update the budget
    @PostMapping
    public Budget setBudget(@RequestBody Budget budget) {
        return budgetService.setBudget(budget);
    }

    // Endpoint to get budget by category (with or without year/month, based on the logic in service)
    @GetMapping("/default/{category}")
    public Optional<Budget> getBudget(@PathVariable String category) {
        return budgetService.getBudgetByCategory(category);
    }

    // Endpoint to get budget by category (with or without year/month, based on the logic in service)
    // @GetMapping("/{category}")
    // public Optional<Budget> getBudget2(@PathVariable String category) {
    //     return budgetService.getBudgetByCategory(category);
    // }

    // Get budget by category, year, and month
    @GetMapping("/{category}/{year}/{month}")
    public Optional<Budget> getBudget(@PathVariable String category,
                                      @PathVariable int year,
                                      @PathVariable int month) {
        return budgetService.getBudgetByCategoryAndYearAndMonth(category, year, month);
    }

    // Endpoint to delete a budget by ID
    @DeleteMapping("/{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }

    // New endpoint to check if the expense exceeds the budget
    @GetMapping("/check/{category}/{year}/{month}/{expenseAmount}")
    public String checkBudgetExceed(@PathVariable String category,
                                    @PathVariable int year,
                                    @PathVariable int month,
                                    @PathVariable double expenseAmount) {
        return budgetService.checkIfBudgetExceeded(category, year, month, expenseAmount);
    }
}
