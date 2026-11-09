package org.example.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Database entity: each object of this class is one row of the "expenses" table.
 * An expense (for example "Light" for 80$) belongs to one house and is shared by its members.
 */
@Entity
@Table(name = "expenses")
public class Expense {

    // Primary key, generated automatically by the database (auto increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // House that pays this expense. Foreign key house_id -> houses.id
    @ManyToOne(optional = false)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    // Name of the expense, for example "Light" (required)
    @Column(nullable = false, length = 100)
    private String name;

    // Total amount of the expense, with 2 decimals (required)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // JPA needs an empty constructor to create objects from database rows
    public Expense() {
    }

    // Constructor used when registering a new expense
    public Expense(House house, String name, BigDecimal amount) {
        this.house = house;
        this.name = name;
        this.amount = amount;
    }

    public Long getId() { return id; }

    public House getHouse() { return house; }

    public String getName() { return name; }

    public BigDecimal getAmount() { return amount; }
}
