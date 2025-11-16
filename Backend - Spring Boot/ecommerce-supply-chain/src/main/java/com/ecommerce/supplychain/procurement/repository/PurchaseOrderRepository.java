package com.ecommerce.supplychain.procurement.repository;

import com.ecommerce.supplychain.procurement.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Purchase Order entity.
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByPoNumber(String poNumber);

    List<PurchaseOrder> findBySupplierId(Long supplierId);

    List<PurchaseOrder> findByStatus(String status);

    List<PurchaseOrder> findByContractId(Long contractId);

    boolean existsByPoNumber(String poNumber);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.expectedDeliveryDate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findByExpectedDeliveryDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'APPROVED' AND po.expectedDeliveryDate < :date")
    List<PurchaseOrder> findOverduePurchaseOrders(@Param("date") LocalDate date);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.requestedBy = :user")
    List<PurchaseOrder> findByRequestedBy(@Param("user") String user);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.orderDate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findByOrderDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}