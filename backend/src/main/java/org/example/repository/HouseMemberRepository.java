package org.example.repository;

import org.example.entity.HouseMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Database access for house memberships.
 * Spring generates the SQL for the methods declared below from their names.
 */
public interface HouseMemberRepository extends JpaRepository<HouseMember, Long> {

    // Returns the membership of a user (the house he/she belongs to), or empty if none
    // SQL generated: SELECT * FROM house_members m WHERE m.user_id = ?
    Optional<HouseMember> findByUserId(Long userId);

    // Returns true if the user already belongs to a house
    // SQL generated: SELECT count(*) > 0 FROM house_members m WHERE m.user_id = ?
    boolean existsByUserId(Long userId);

    // Returns how many users belong to a house
    // SQL generated: SELECT count(*) FROM house_members m WHERE m.house_id = ?
    long countByHouseId(Long houseId);
}
