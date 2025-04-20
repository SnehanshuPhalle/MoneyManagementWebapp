package com.money.money_management.service;


import com.money.money_management.model.Budget;
import com.money.money_management.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    // Set or update the budget with year and month
    public Budget setBudget(Budget budget) {
        // If it's a default budget, ensure we have only one default for the category
        if (budget.isDefault()) {
            Optional<Budget> existingDefaultBudget = budgetRepository.findByCategoryAndIsDefaultTrue(budget.getCategory());
            existingDefaultBudget.ifPresent(b -> {
                b.setDefault(false); // Disable the old default if a new default is set
                budgetRepository.save(b); // Save the old default
            });
        }
        return budgetRepository.save(budget);
    }

    // Get budget by category, year, and month
    public Optional<Budget> getBudgetByCategoryAndYearAndMonth(String category, int year, int month) {
        return budgetRepository.findByCategoryAndYearAndMonth(category, year, month);
    }

    // Get budget by category for default budget
    public Optional<Budget> getDefaultBudgetByCategory(String category) {
        return budgetRepository.findByCategoryAndIsDefaultTrue(category);
    }

    // Get budget by category for default budget
    public Optional<Budget> getBudgetByCategory(String category) {
        return budgetRepository.findByCategory(category);
    }

    // Delete budget by ID
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    // Get all budgets for a specific month and year
    public List<Budget> getBudgetsForMonthAndYear(int year, int month) {
        return budgetRepository.findByYearAndMonth(year, month);
    }

    // New method to check if the expense exceeds the budget
    public String checkIfBudgetExceeded(String category, int year, int month, double expenseAmount) {
        Optional<Budget> budgetOpt = getBudgetByCategoryAndYearAndMonth(category, year, month);

        if (!budgetOpt.isPresent()) {
            // If no specific budget exists for the month, check for default budget
            Optional<Budget> defaultBudgetOpt = getDefaultBudgetByCategory(category);
            if (defaultBudgetOpt.isPresent()) {
                Budget defaultBudget = defaultBudgetOpt.get();
                if (expenseAmount > defaultBudget.getLimitAmount()) {
                    return "Warning: The expense exceeds the default budget limit of " + defaultBudget.getLimitAmount() + " for " +
                            category;
                }
            }
            return "No budget found for " + category + " in " + month + "/" + year;
        }

        Budget budget = budgetOpt.get();
        if (expenseAmount > budget.getLimitAmount()) {
            return "Warning: The expense exceeds the budget limit of " + budget.getLimitAmount() + " for " +
                    category + " in " + month + "/" + year;
        } else {
            return "The expense is within the budget limit for " + category + " in " + month + "/" + year;
        }
    }
}
