package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Database access for users.
 * By extending JpaRepository we get save, findById, findAll, delete... for free.
 * Spring generates the SQL for the methods declared below from their names.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Returns true if a user with this email exists
    // SQL generated: SELECT count(*) > 0 FROM users WHERE email = ?
    boolean existsByEmail(String email);

    // Returns the user with this email, or an empty Optional if there is none
    // SQL generated: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
}
