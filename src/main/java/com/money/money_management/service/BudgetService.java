package com.money.money_management.service;

import com.money.money_management.model.Budget;
import com.money.money_management.model.User;
import com.money.money_management.repository.BudgetRepository;
import com.money.money_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    // Set or update the budget with year and month for a specific user
    public Budget setBudget(Budget budget, Long userId) {
        // Fetch the user and associate it with the budget
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        budget.setUser(user);

        // If it's a default budget, ensure we have only one default for the category for the user
        if (budget.isDefault()) {
            Optional<Budget> existingDefaultBudget = budgetRepository.findByCategoryAndIsDefaultTrueAndUser(budget.getCategory(), user);
            existingDefaultBudget.ifPresent(b -> {
                b.setDefault(false); // Disable the old default if a new default is set
                budgetRepository.save(b); // Save the old default
            });
        }
        return budgetRepository.save(budget);
    }

    // Get budget by category, year, and month for a specific user
    public Optional<Budget> getBudgetByCategoryAndYearAndMonth(String category, int year, int month, Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        return budgetRepository.findByCategoryAndYearAndMonthAndUser(category, year, month, user);
    }

    // Get budget by category for default budget for a specific user
    public Optional<Budget> getDefaultBudgetByCategory(String category, Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        return budgetRepository.findByCategoryAndIsDefaultTrueAndUser(category, user);
    }

    // Get budget by category for a specific user
    public Optional<Budget> getBudgetByCategory(String category, Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        return budgetRepository.findByCategoryAndUser(category, user);
    }

    // Delete budget by ID for a specific user
    public void deleteBudget(Long id, Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Budget> budgetOpt = budgetRepository.findByIdAndUser(id, user);
        if (budgetOpt.isPresent()) {
            budgetRepository.deleteById(id);
        } else {
            throw new RuntimeException("Budget not found or doesn't belong to the user");
        }
    }

    // Get all budgets for a specific month and year for a specific user
    public List<Budget> getBudgetsForMonthAndYear(int year, int month, Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        return budgetRepository.findByYearAndMonthAndUser(year, month, user);
    }

    // New method to check if the expense exceeds the budget for a specific user
    public String checkIfBudgetExceeded(String category, int year, int month, double expenseAmount, Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Budget> budgetOpt = getBudgetByCategoryAndYearAndMonth(category, year, month, userId);

        if (!budgetOpt.isPresent()) {
            // If no specific budget exists for the month, check for default budget
            Optional<Budget> defaultBudgetOpt = getDefaultBudgetByCategory(category, userId);
            if (defaultBudgetOpt.isPresent()) {
                Budget defaultBudget = defaultBudgetOpt.get();
                if (expenseAmount > defaultBudget.getLimitAmount()) {
                    return "Warning: The expense exceeds the default budget limit of " + defaultBudget.getLimitAmount() + " for " +
                            category;
                }
            }
            return "No budget found for " + category + " in " + month + "/" + year + " for user " + userId;
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
