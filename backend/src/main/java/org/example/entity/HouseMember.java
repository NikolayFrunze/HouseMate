package org.example.entity;

import jakarta.persistence.*;

/**
 * Database entity: each row of the "house_members" table says that a user lives in a house.
 * A house can have many members, and the membership is what decides whether a user
 * belongs to a house (and therefore can see its expenses).
 */
@Entity
@Table(name = "house_members")
public class HouseMember {

    // Primary key, generated automatically by the database (auto increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // House the user belongs to. Foreign key house_id -> houses.id
    @ManyToOne(optional = false)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    // The member. unique = true: a user can only belong to one house.
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // JPA needs an empty constructor to create objects from database rows
    public HouseMember() {
    }

    // Constructor used when a user joins a house
    public HouseMember(House house, User user) {
        this.house = house;
        this.user = user;
    }

    public Long getId() { return id; }

    public House getHouse() { return house; }

    public User getUser() { return user; }
}
