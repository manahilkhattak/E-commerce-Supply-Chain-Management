package com.ecommerce.supplychain.forecasting.repository;

import com.ecommerce.supplychain.forecasting.model.ReorderPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ReorderPlan entity.
 */
@Repository
public interface ReorderPlanRepository extends JpaRepository<ReorderPlan, Long> {

    List<ReorderPlan> findByProductId(Long productId);

    List<ReorderPlan> findByPlanStatus(String planStatus);

    List<ReorderPlan> findByOrderUrgency(String orderUrgency);

    List<ReorderPlan> findByStockoutRiskLevel(String stockoutRiskLevel);

    List<ReorderPlan> findByConvertedToPo(Boolean convertedToPo);

    List<ReorderPlan> findBySupplierId(Long supplierId);

    Optional<ReorderPlan> findByForecastId(Long forecastId);

    @Query("SELECT rp FROM ReorderPlan rp WHERE rp.convertedToPo = false AND rp.suggestedOrderDate <= :date")
    List<ReorderPlan> findPendingReorderPlans(@Param("date") LocalDate date);

    @Query("SELECT rp FROM ReorderPlan rp WHERE rp.convertedToPo = false AND rp.orderUrgency = 'CRITICAL'")
    List<ReorderPlan> findCriticalReorderPlans();

    @Query("SELECT rp FROM ReorderPlan rp WHERE rp.expectedStockoutDate BETWEEN :startDate AND :endDate")
    List<ReorderPlan> findPlansWithExpectedStockout(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT rp FROM ReorderPlan rp WHERE rp.productId = :productId AND rp.convertedToPo = false")
    List<ReorderPlan> findActivePlansByProduct(@Param("productId") Long productId);
}