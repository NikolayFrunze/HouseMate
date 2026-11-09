package org.example.repository;

import org.example.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Database access for expenses.
 * Spring generates the SQL for the methods declared below from their names.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Returns all the expenses of a house, oldest first
    // SQL generated: SELECT * FROM expenses e WHERE e.house_id = ? ORDER BY e.id ASC
    List<Expense> findByHouseIdOrderByIdAsc(Long houseId);
}
