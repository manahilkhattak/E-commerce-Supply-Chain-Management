package com.ecommerce.supplychain.receiving.repository;

import com.ecommerce.supplychain.receiving.model.InspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Inspection Record entity.
 */
@Repository
public interface InspectionRepository extends JpaRepository<InspectionRecord, Long> {

    List<InspectionRecord> findByGoodsReceipt_ReceiptId(Long receiptId);

    List<InspectionRecord> findByInspectionStatus(String status);

    List<InspectionRecord> findByInspectorName(String inspectorName);

    List<InspectionRecord> findByProductId(Long productId);

    @Query("SELECT ir FROM InspectionRecord ir WHERE ir.rejectedQuantity > 0")
    List<InspectionRecord> findRecordsWithRejections();

    @Query("SELECT ir FROM InspectionRecord ir WHERE ir.qualityRating < 3")
    List<InspectionRecord> findLowQualityRecords();
}