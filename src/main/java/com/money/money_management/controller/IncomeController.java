package com.money.money_management.controller;

import com.money.money_management.model.Income;
import com.money.money_management.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@CrossOrigin
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    // Get all incomes
    @GetMapping
    public List<Income> getAllIncomes() {
        return incomeService.getAllIncomes();
    }

    // Get income by ID
    @GetMapping("/{id}")
    public Income getIncomeById(@PathVariable Long id) {
        return incomeService.getIncomeById(id);
    }

    // Create new income
    @PostMapping
    public Income createIncome(@RequestBody Income income) {
        return incomeService.createIncome(income);
    }

    // Update existing income
    @PutMapping("/{id}")
    public Income updateIncome(@PathVariable Long id, @RequestBody Income income) {
        return incomeService.updateIncome(id, income);
    }

    // Delete income
    @DeleteMapping("/{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }

    // Get incomes by user ID
    @GetMapping("/user/{userId}")
    public List<Income> getIncomesByUser(@PathVariable Long userId) {
        return incomeService.getIncomesByUser(userId);
    }

    // Get incomes by source (e.g., "SALARY")
    @GetMapping("/source/{source}")
    public List<Income> getIncomesBySource(@PathVariable String source) {
        return incomeService.getIncomesBySource(source);
    }

    // Get incomes for a specific user and month/year
    @GetMapping("/user/{userId}/month/{month}/year/{year}")
    public List<Income> getIncomesByUserAndMonthYear(@PathVariable Long userId, @PathVariable int month, @PathVariable int year) {
        return incomeService.getIncomesByUserAndMonthYear(userId, month, year);
    }

    // Get total income for a user in a specific year
    @GetMapping("/user/{userId}/total/year/{year}")
    public Double getTotalIncomeByUserAndYear(@PathVariable Long userId, @PathVariable int year) {
        return incomeService.getTotalIncomeByUserAndYear(userId, year);
    }

    // Get incomes within a date range
    @GetMapping("/dates")
    public List<Income> getIncomesBetweenDates(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return incomeService.getIncomesBetweenDates(startDate, endDate);
    }
}