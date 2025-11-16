package com.ecommerce.supplychain.returns.repository;

import com.ecommerce.supplychain.returns.model.ReturnOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnOrderRepository extends JpaRepository<ReturnOrder, Long> {

    Optional<ReturnOrder> findByReturnNumber(String returnNumber);

    List<ReturnOrder> findByOrderId(Long orderId);

    List<ReturnOrder> findByCustomerId(Long customerId);

    List<ReturnOrder> findByReturnStatus(String returnStatus);

    List<ReturnOrder> findByReturnType(String returnType);

    List<ReturnOrder> findByReturnReason(String returnReason);

    List<ReturnOrder> findByWarehouseId(Long warehouseId);

    boolean existsByOrderIdAndReturnStatusIn(Long orderId, List<String> statuses);

    @Query("SELECT ro FROM ReturnOrder ro WHERE ro.requestDate BETWEEN :startDate AND :endDate")
    List<ReturnOrder> findReturnsByDateRange(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ro FROM ReturnOrder ro WHERE ro.returnStatus IN ('REQUESTED', 'APPROVED', 'RECEIVED', 'INSPECTING')")
    List<ReturnOrder> findActiveReturns();

    @Query("SELECT ro FROM ReturnOrder ro WHERE ro.pickupRequired = true AND ro.pickupCompletedDate IS NULL")
    List<ReturnOrder> findReturnsRequiringPickup();

    @Query("SELECT ro FROM ReturnOrder ro WHERE ro.refundStatus = 'PENDING' AND ro.returnStatus = 'COMPLETED'")
    List<ReturnOrder> findReturnsPendingRefund();

    @Query("SELECT COUNT(ro) FROM ReturnOrder ro WHERE ro.returnStatus = 'REQUESTED'")
    Long countPendingApprovalReturns();

    @Query("SELECT ro FROM ReturnOrder ro WHERE ro.isRestockable = true AND ro.returnStatus = 'COMPLETED'")
    List<ReturnOrder> findCompletedRestockableReturns();
}