package org.example.entity;

import jakarta.persistence.*;

/**
 * Database entity: each object of this class is one row of the "houses" table.
 * Every house belongs to exactly one user (user_id is a foreign key to users.id).
 */
@Entity
@Table(name = "houses")
public class House {

    // Primary key, generated automatically by the database (auto increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Owner of the house. Foreign key user_id -> users.id.
    // unique = true: a user can only have one house.
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Street address (required)
    @Column(nullable = false)
    private String address;

    // Postal code (required)
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    // Housing code of the flat/house (required)
    @Column(name = "housing_code", nullable = false)
    private String housingCode;

    // joinCode to join in a new house
    @Column(name = "join_code", nullable = false, unique = true)
    private String joinCode;

    // JPA needs an empty constructor to create objects from database rows
    public House() {
    }

    // Constructor used when registering a new house
    public House(User user, String address, String postalCode, String housingCode, String joinCode) {
        this.user = user;
        this.address = address;
        this.postalCode = postalCode;
        this.housingCode = housingCode;
        this.joinCode = joinCode;
    }

    // Getters: read the values of the fields
    public Long getId() { return id; }

    public User getUser() { return user; }

    public String getAddress() { return address; }

    public String getPostalCode() { return postalCode; }

    public String getHousingCode() { return housingCode; }
    public String getJoinCode() { return joinCode; }
}
