package org.example.repository;

import org.example.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Database access for houses.
 * Spring generates the SQL for the methods declared below from their names.
 */
public interface HouseRepository extends JpaRepository<House, Long> {

    // Returns the house of a user, or an empty Optional if the user has none
    // SQL generated: SELECT * FROM houses h WHERE h.user_id = ?
    Optional<House> findByUserId(Long userId);

    // Returns true if the user already has a house
    // SQL generated: SELECT count(*) > 0 FROM houses h WHERE h.user_id = ?
    boolean existsByUserId(Long userId);
    Optional<House> findByJoinCode(String joinCode);
    boolean existsByJoinCode(String joinCode);
}
