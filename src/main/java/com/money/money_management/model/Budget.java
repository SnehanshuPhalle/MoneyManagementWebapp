package com.money.money_management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private Double limitAmount; // This is the field for the budget limit amount.

    private int year; // Store the year for monthly budgets.

    private int month; // Store the month for monthly budgets.

    private boolean isDefault; // Flag to indicate if this is a default budget.

    @ManyToOne(fetch = FetchType.LAZY) // Many budgets can belong to one user
    @JoinColumn(name = "user_id") // Foreign key column for user
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user; // This represents the user associated with the budget.

    // Constructors
    public Budget() {}

    public Budget(String category, Double limitAmount, int year, int month, boolean isDefault, User user) {
        this.category = category;
        this.limitAmount = limitAmount;
        this.year = year;
        this.month = month;
        this.isDefault = isDefault;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
