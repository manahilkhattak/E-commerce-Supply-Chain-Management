package com.ecommerce.supplychain.inventory.repository;

import com.ecommerce.supplychain.inventory.model.StockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for StockAlert entity.
 */
@Repository
public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {

    List<StockAlert> findByProductId(Long productId);

    List<StockAlert> findByAlertType(String alertType);

    List<StockAlert> findByAlertLevel(String alertLevel);

    List<StockAlert> findByIsResolved(Boolean isResolved);

    List<StockAlert> findByNotificationSent(Boolean notificationSent);

    @Query("SELECT sa FROM StockAlert sa WHERE sa.isResolved = false AND sa.createdAt < :date")
    List<StockAlert> findOldUnresolvedAlerts(@Param("date") LocalDateTime date);

    @Query("SELECT sa FROM StockAlert sa WHERE sa.isResolved = false AND sa.alertLevel = 'CRITICAL'")
    List<StockAlert> findCriticalUnresolvedAlerts();

    @Query("SELECT sa FROM StockAlert sa WHERE sa.productId = :productId AND sa.isResolved = false")
    List<StockAlert> findActiveAlertsByProduct(@Param("productId") Long productId);

    @Query("SELECT sa FROM StockAlert sa WHERE sa.createdAt BETWEEN :startDate AND :endDate")
    List<StockAlert> findAlertsByDateRange(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}