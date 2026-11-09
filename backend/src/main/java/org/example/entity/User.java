package org.example.entity;

import jakarta.persistence.*;

/**
 * Database entity: each object of this class is one row of the "users" table.
 * Hibernate (JPA) creates the table and maps the fields to its columns.
 */
@Entity
@Table(name = "users")
public class User {

    // Primary key, generated automatically by the database (auto increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the user (required)
    @Column(nullable = false)
    private String username;

    // Email of the user (required and unique: two users cannot have the same email)
    @Column(nullable = false, unique = true)
    private String email;

    // Password, stored already encrypted with BCrypt (required)
    @Column(nullable = false)
    private String password;

    // JPA needs an empty constructor to create objects from database rows
    public User() {
    }

    // Constructor used when creating a new user
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters: read the values of the fields (there are no setters on purpose)
    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}
