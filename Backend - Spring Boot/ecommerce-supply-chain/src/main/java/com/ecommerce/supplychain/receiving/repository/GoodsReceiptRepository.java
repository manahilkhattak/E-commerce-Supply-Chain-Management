package com.ecommerce.supplychain.receiving.repository;

import com.ecommerce.supplychain.receiving.model.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Goods Receipt entity.
 */
@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {

    Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber);

    List<GoodsReceipt> findByPoId(Long poId);

    List<GoodsReceipt> findBySupplierId(Long supplierId);

    List<GoodsReceipt> findByStatus(String status);

    List<GoodsReceipt> findByReceivedBy(String receivedBy);

    boolean existsByReceiptNumber(String receiptNumber);

    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.receiptDate BETWEEN :startDate AND :endDate")
    List<GoodsReceipt> findByReceiptDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.discrepancyFound = true")
    List<GoodsReceipt> findReceiptsWithDiscrepancies();

    List<GoodsReceipt> findByWarehouseLocation(String warehouseLocation);
}