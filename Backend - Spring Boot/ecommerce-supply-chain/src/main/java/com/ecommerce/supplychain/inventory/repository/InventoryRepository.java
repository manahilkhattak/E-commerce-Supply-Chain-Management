package com.ecommerce.supplychain.inventory.repository;

import com.ecommerce.supplychain.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Inventory entity.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    Optional<Inventory> findByProductSku(String productSku);

    List<Inventory> findByStockStatus(String stockStatus);

    List<Inventory> findByIsMonitored(Boolean isMonitored);

    List<Inventory> findByMovementFrequency(String movementFrequency);

    @Query("SELECT i FROM Inventory i WHERE i.currentStock <= i.reorderPoint AND i.isMonitored = true")
    List<Inventory> findLowStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.currentStock = 0 AND i.isMonitored = true")
    List<Inventory> findOutOfStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.currentStock > i.maximumStockLevel * 0.9 AND i.isMonitored = true")
    List<Inventory> findOverstockItems();

    @Query("SELECT i FROM Inventory i WHERE i.availableStock < i.minimumStockLevel AND i.isMonitored = true")
    List<Inventory> findItemsBelowMinimum();

    @Query("SELECT i FROM Inventory i WHERE i.stockTurnoverRate < :threshold AND i.isMonitored = true")
    List<Inventory> findSlowMovingItems(@Param("threshold") Double threshold);

    @Query("SELECT i FROM Inventory i WHERE i.daysOfSupply < :daysThreshold AND i.isMonitored = true")
    List<Inventory> findLowCoverageItems(@Param("daysThreshold") Integer daysThreshold);

    @Query("SELECT i FROM Inventory i WHERE i.productName LIKE %:searchTerm% OR i.productSku LIKE %:searchTerm%")
    List<Inventory> searchInventory(@Param("searchTerm") String searchTerm);
}