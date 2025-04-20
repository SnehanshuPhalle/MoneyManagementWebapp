package com.money.money_management.controller;

import com.money.money_management.repository.ExpenseRepository;
import com.money.money_management.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/summary")
@CrossOrigin
public class SummaryController {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping
    public Map<String, Double> getSummary() {
        Double totalIncome = incomeRepository.findAll()
                .stream()
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0.0)
                .sum();

        Double totalExpense = expenseRepository.findAll()
                .stream()
                .mapToDouble(e -> e.getAmount() != null ? e.getAmount() : 0.0)
                .sum();

        Map<String, Double> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("netBalance", totalIncome - totalExpense);

        return summary;
    }
}
