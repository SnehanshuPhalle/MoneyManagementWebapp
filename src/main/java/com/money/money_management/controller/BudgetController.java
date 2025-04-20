package com.money.money_management.controller;

import com.money.money_management.model.Budget;
import com.money.money_management.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // ✅ Set or update budget (POST /api/budget?userId=1)
    @PostMapping
    public Budget setBudget(@RequestBody Budget budget, @RequestParam Long userId) {
        return budgetService.setBudget(budget, userId);
    }

    // ✅ Get default budget by category (GET /api/budget/default/Food?userId=1)
    @GetMapping("/default/{category}")
    public Optional<Budget> getDefaultBudget(@PathVariable String category, @RequestParam Long userId) {
        return budgetService.getDefaultBudgetByCategory(category, userId);
    }

    // ✅ Get budget by category, year, and month (GET /api/budget/Food/2025/4?userId=1)
    @GetMapping("/{category}/{year}/{month}")
    public Optional<Budget> getBudget(@PathVariable String category,
                                      @PathVariable int year,
                                      @PathVariable int month,
                                      @RequestParam Long userId) {
        return budgetService.getBudgetByCategoryAndYearAndMonth(category, year, month, userId);
    }

    // ✅ Delete a budget by ID (DELETE /api/budget/3?userId=1)
    @DeleteMapping("/{id}")
    public void deleteBudget(@PathVariable Long id, @RequestParam Long userId) {
        budgetService.deleteBudget(id, userId);
    }

    // ✅ Check if the expense exceeds the budget (GET /api/budget/check/Food/2025/4/5500?userId=1)
    @GetMapping("/check/{category}/{year}/{month}/{expenseAmount}")
    public String checkBudgetExceed(@PathVariable String category,
                                    @PathVariable int year,
                                    @PathVariable int month,
                                    @PathVariable double expenseAmount,
                                    @RequestParam Long userId) {
        return budgetService.checkIfBudgetExceeded(category, year, month, expenseAmount, userId);
    }

}
