package com.money.money_management.service;

import com.money.money_management.model.Budget;
import com.money.money_management.model.Expense;
import com.money.money_management.model.User;
import com.money.money_management.repository.ExpenseRepository;
import com.money.money_management.repository.UserRepository;
import com.money.money_management.util.CategoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserRepository userRepository;

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

        // Ensure user is set (optional: you can load the user from DB for integrity)
        if (expense.getUser() == null) {
            System.out.println("⚠️ User is missing in the request.");
            return null;
        }
        if (expense.getUser().getId() == 0) {
            System.out.println("⚠️ Invalid User ID (zero)");
            return null;
        }

        Optional<User> userOpt = userRepository.findById(expense.getUser().getId());
        if (userOpt.isEmpty()) {
            System.out.println("❌ Invalid User ID: " + expense.getUser().getId());
            return null;
        }

        // Set the full user object to expense
        expense.setUser(userOpt.get());

        // Get current year and month
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        // Budget check by category (and optionally user)
        String category = expense.getCategory();
        Optional<Budget> budgetOpt = budgetService.getBudgetByCategoryAndYearAndMonth(category, year, month);

        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            Double limit = budget.getLimitAmount();

            // Filter expenses for same category and user (if user is present)
            List<Expense> allExpenses = expenseRepository.findAll();
            double categoryTotal = allExpenses.stream()
                    .filter(e -> e.getCategory().equalsIgnoreCase(category))
                    .filter(e -> expense.getUser() == null ||
                            (e.getUser() != null && e.getUser().getId() == expense.getUser().getId()))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            if (categoryTotal + expense.getAmount() > limit) {
                System.out.println("⚠️ Budget exceeded for category: " + category);
                // You can throw a custom exception or return some warning status if needed
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
        existing.setDescription(updatedExpense.getDescription());

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

    // 📊 MONTHLY SUMMARY PER USER
    public Map<String, Double> getMonthlySummaryByUser(Long userId, int year, int month) {
        return expenseRepository.findAll().stream()
                .filter(e -> e.getUser() != null && e.getUser().getId() == userId)
                .filter(e -> e.getDate().getYear() == year && e.getDate().getMonthValue() == month)
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    // 📊 YEARLY SUMMARY PER USER
    public Map<String, Double> getYearlySummaryByUser(Long userId, int year) {
        return expenseRepository.findAll().stream()
                .filter(e -> e.getUser() != null && e.getUser().getId() == userId)
                .filter(e -> e.getDate().getYear() == year)
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    public List<Expense> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    public Double getCategoryTotalForMonth(String category, int year, int month) {
        return expenseRepository.getCategoryTotalByMonthAndYear(category, year, month);
    }

    public Double getUserTotalForMonth(Long userId, int year, int month) {
        return expenseRepository.getUserTotalByMonthAndYear(userId, year, month);
    }

    // 📈 GRAPH-READY: Monthly Trend (Category-wise) for a Year
    public Map<Integer, Map<String, Double>> getMonthlyCategoryTrend(Long userId, int year) {
        List<Expense> userExpenses = expenseRepository.findAll().stream()
                .filter(e -> e.getUser() != null && e.getUser().getId() == userId)
                .filter(e -> e.getDate().getYear() == year)
                .toList();

        Map<Integer, Map<String, Double>> trend = new HashMap<>();

        for (Expense e : userExpenses) {
            int month = e.getDate().getMonthValue();
            String category = e.getCategory();
            trend.putIfAbsent(month, new HashMap<>());
            Map<String, Double> categoryMap = trend.get(month);
            categoryMap.put(category, categoryMap.getOrDefault(category, 0.0) + e.getAmount());
        }

        return trend;
    }
}
