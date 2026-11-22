package com.example.housing.repository;

import com.example.housing.entity.User; //entity that this repo is for
import org.springframework.data.jpa.repository.JpaRepository; //provides crud operations
public interface UserRepository extends JpaRepository<User, Long> { //repo for user where id is long. interface is like a contract. JpaRepository provides crud operations for user entity. JpaRepository is a generic interface that takes two parameters: the type of the entity and the type of the entity's primary key.
}
