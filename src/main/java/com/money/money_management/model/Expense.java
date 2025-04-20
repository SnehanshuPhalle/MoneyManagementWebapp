package com.money.money_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;         // e.g., "Groceries"
    private Double amount;        // e.g., 150.75
    private String category;      // e.g., "Food", "Transport"
    private LocalDate date;       // e.g., 2025-04-18
}
