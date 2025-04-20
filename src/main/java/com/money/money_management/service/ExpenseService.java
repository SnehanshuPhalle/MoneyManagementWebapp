package com.money.money_management.service;

import com.money.money_management.model.Budget;
import com.money.money_management.model.Expense;
import com.money.money_management.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.money.money_management.util.CategoryUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetService budgetService;  // Make sure this is auto-wired

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense createExpense(Expense expense) {
        // Auto-categorize if no category is provided
        if (expense.getCategory() == null || expense.getCategory().isBlank()) {
            String autoCategory = CategoryUtils.detectCategory(expense.getTitle());
            expense.setCategory(autoCategory);
        }

        // Get the current year and month
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;  // Month is 0-indexed in Calendar

        // Check if budget limit is exceeded
        String category = expense.getCategory();
        Optional<Budget> budgetOpt = budgetService.getBudgetByCategoryAndYearAndMonth(category, year, month);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            Double limit = budget.getLimitAmount();

            // Calculate current total for the category
            List<Expense> allExpenses = expenseRepository.findAll();
            double categoryTotal = allExpenses.stream()
                    .filter(e -> e.getCategory().equalsIgnoreCase(category))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            if (categoryTotal + expense.getAmount() > limit) {
                System.out.println("⚠️ Budget exceeded for category: " + category);
                // Optionally: throw exception or return warning
            }
        }

        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense existing = getExpenseById(id);
        if (existing == null) return null;

        existing.setTitle(updatedExpense.getTitle());
        existing.setAmount(updatedExpense.getAmount());
        existing.setCategory(updatedExpense.getCategory());
        existing.setDate(updatedExpense.getDate());

        return expenseRepository.save(existing);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Map<String, Double> getCategorySummary() {
        List<Expense> expenses = expenseRepository.findAll();
        Map<String, Double> categorySummary = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categorySummary.put(category, categorySummary.getOrDefault(category, 0.0) + expense.getAmount());
        }

        return categorySummary;
    }
}
