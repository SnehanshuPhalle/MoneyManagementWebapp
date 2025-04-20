package com.money.money_management.service;

import com.money.money_management.model.Income;
import com.money.money_management.model.User;
import com.money.money_management.repository.IncomeRepository;
import com.money.money_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all incomes
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    // Get income by ID
    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
    }

    // Create a new income (must be linked to an existing user)
    public Income createIncome(Income income) {
        // Validate if user exists
        if (income.getUser() == null || income.getUser().getId() == 0) {
            throw new RuntimeException("User ID is required");
        }

        User user = userRepository.findById(income.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        income.setUser(user);
        return incomeRepository.save(income);
    }

    // Update income
    public Income updateIncome(Long id, Income updatedIncome) {
        Income existingIncome = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        existingIncome.setSource(updatedIncome.getSource());
        existingIncome.setAmount(updatedIncome.getAmount());
        existingIncome.setDate(updatedIncome.getDate());

        // Update user if provided
        if (updatedIncome.getUser() != null) {
            User user = userRepository.findById(updatedIncome.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingIncome.setUser(user);
        }

        return incomeRepository.save(existingIncome);
    }

    // Delete income
    public void deleteIncome(Long id) {
        if (!incomeRepository.existsById(id)) {
            throw new RuntimeException("Income not found");
        }
        incomeRepository.deleteById(id);
    }
}
