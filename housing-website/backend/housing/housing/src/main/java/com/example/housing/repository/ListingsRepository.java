package com.example.housing.repository;
import com.example.housing.entity.Listings; //entity that this repo is for
import org.springframework.data.jpa.repository.JpaRepository; //provides crud operations
import java.util.List;

public interface ListingsRepository extends JpaRepository<Listings, Long> { //repo for house where id is long. interface is like a contract. JpaRepository provides crud operations for house entity. JpaRepository is a generic interface that takes two parameters: the type of the entity and the type of the entity's primary key.
List<Listings> findByRentLessThanEqual(int maxRent);

List<Listings> findByRooms(int rooms);

List<Listings> findByRentLessThanEqualAndRooms(int maxRent, int rooms);

boolean existsByNameAndAddress(String name, String address);}
