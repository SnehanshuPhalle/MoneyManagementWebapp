package com.money.money_management.controller;

import com.money.money_management.model.Expense;
import com.money.money_management.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Get all expenses
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    // Get an expense by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // 404 if not found
        }
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    // Get category-wise summary of all expenses
    @GetMapping("/summary")
    public Map<String, Double> getCategorySummary() {
        return expenseService.getCategorySummary();
    }

    // Get category-wise summary for Pie Chart (same as summary in this case)
    @GetMapping("/category-pie-chart")
    public Map<String, Double> getCategoryPieChartData() {
        return expenseService.getCategorySummary();
    }

    // Create a new expense
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Expense createdExpense = expenseService.createExpense(expense);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);  // 201 Created
    }

    // Update an existing expense
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        Expense updatedExpense = expenseService.updateExpense(id, expense);
        if (updatedExpense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // 404 if expense not found
        }
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    // Delete an expense by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        Expense existingExpense = expenseService.getExpenseById(id);
        if (existingExpense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // 404 if not found
        }
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content for successful deletion
    }

    // Get monthly summary for a specific user
    @GetMapping("/user/{userId}/monthly/{year}/{month}")
    public Map<String, Double> getMonthlySummaryByUser(@PathVariable Long userId, @PathVariable int year, @PathVariable int month) {
        return expenseService.getMonthlySummaryByUser(userId, year, month);
    }

    // Get yearly summary for a specific user
    @GetMapping("/user/{userId}/yearly/{year}")
    public Map<String, Double> getYearlySummaryByUser(@PathVariable Long userId, @PathVariable int year) {
        return expenseService.getYearlySummaryByUser(userId, year);
    }

    // Get expenses by user ID
    @GetMapping("/user/{userId}")
    public List<Expense> getExpensesByUser(@PathVariable Long userId) {
        return expenseService.getExpensesByUser(userId);
    }

    // Get category total for a specific user for a given month
    @GetMapping("/user/{userId}/category/{category}/monthly/{year}/{month}")
    public Double getCategoryTotalForMonth(@PathVariable Long userId, @PathVariable String category, @PathVariable int year, @PathVariable int month) {
        return expenseService.getCategoryTotalForMonth(category, year, month);
    }

    // Get total expense for a user in a given month
    @GetMapping("/user/{userId}/total/monthly/{year}/{month}")
    public Double getUserTotalForMonth(@PathVariable Long userId, @PathVariable int year, @PathVariable int month) {
        return expenseService.getUserTotalForMonth(userId, year, month);
    }

    // Get monthly trend data for a specific user (category-wise)
    @GetMapping("/user/{userId}/monthly-trend/{year}")
    public Map<Integer, Map<String, Double>> getMonthlyCategoryTrend(@PathVariable Long userId, @PathVariable int year) {
        return expenseService.getMonthlyCategoryTrend(userId, year);
    }
}
