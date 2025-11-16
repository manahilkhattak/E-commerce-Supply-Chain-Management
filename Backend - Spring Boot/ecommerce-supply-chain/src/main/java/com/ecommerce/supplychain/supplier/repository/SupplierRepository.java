package com.ecommerce.supplychain.supplier.repository;

import com.ecommerce.supplychain.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Supplier entity.
 * Provides database operations for supplier management.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // Find supplier by email (for duplicate checking)
    Optional<Supplier> findByEmail(String email);

    // Find all suppliers by status
    List<Supplier> findByStatus(String status);

    // Find suppliers by rating greater than or equal to threshold
    List<Supplier> findByRatingGreaterThanEqual(Double rating);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find suppliers by country
    List<Supplier> findByCountry(String country);
}