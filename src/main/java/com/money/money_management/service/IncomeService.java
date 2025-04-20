package com.money.money_management.service;

import com.money.money_management.model.Income;
import com.money.money_management.model.User;
import com.money.money_management.repository.IncomeRepository;
import com.money.money_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    // Existing methods

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
    }

    public Income createIncome(Income income) {
        if (income.getUser() == null || income.getUser().getId() == 0) {
            throw new RuntimeException("User ID is required");
        }

        User user = userRepository.findById(income.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        income.setUser(user);
        return incomeRepository.save(income);
    }

    public Income updateIncome(Long id, Income updatedIncome) {
        Income existingIncome = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        existingIncome.setTitle(updatedIncome.getTitle());
        existingIncome.setDescription(updatedIncome.getDescription());
        existingIncome.setSource(updatedIncome.getSource());
        existingIncome.setAmount(updatedIncome.getAmount());
        existingIncome.setDate(updatedIncome.getDate());

        if (updatedIncome.getUser() != null) {
            User user = userRepository.findById(updatedIncome.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingIncome.setUser(user);
        }

        return incomeRepository.save(existingIncome);
    }

    public void deleteIncome(Long id) {
        if (!incomeRepository.existsById(id)) {
            throw new RuntimeException("Income not found");
        }
        incomeRepository.deleteById(id);
    }

    // Add filtering/query methods below

    // Get incomes by user ID
    public List<Income> getIncomesByUser(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    // Get incomes by source (e.g., "SALARY", "FREELANCE")
    public List<Income> getIncomesBySource(String source) {
        return incomeRepository.findBySource(source);
    }

    // Get incomes for a specific month and year
    public List<Income> getIncomesByUserAndMonthYear(Long userId, int month, int year) {
        return incomeRepository.findByUserAndMonthYear(userId, month, year);
    }

    // Get total income for a user in a specific year
    public Double getTotalIncomeByUserAndYear(Long userId, int year) {
        return incomeRepository.getTotalIncomeByUserAndYear(userId, year);
    }

    // Get incomes between a date range
    public List<Income> getIncomesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findByDateBetween(startDate, endDate);
    }
}