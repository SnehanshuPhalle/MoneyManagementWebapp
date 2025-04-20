package com.money.money_management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String description;   // e.g., "Groceries for the Week"
    private Double amount;        // e.g., 150.75
    private String category;      // e.g., "Food", "Transport"
    private LocalDate date;       // e.g., 2025-04-18

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user; // Assuming a User entity for association

}
