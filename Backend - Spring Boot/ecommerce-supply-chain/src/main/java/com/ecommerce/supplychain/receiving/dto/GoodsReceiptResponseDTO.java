package com.ecommerce.supplychain.receiving.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for Goods Receipt information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptResponseDTO {

    private Long receiptId;
    private String receiptNumber;
    private Long poId;
    private String poNumber;
    private Long supplierId;
    private LocalDate receiptDate;
    private String receivedBy;
    private String warehouseLocation;
    private String deliveryNoteNumber;
    private String vehicleNumber;
    private String status;
    private Integer totalItemsOrdered;
    private Integer totalItemsReceived;
    private Boolean discrepancyFound;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totalInspectionRecords;
    private List<InspectionRecordResponseDTO> inspectionRecords;

    /**
     * Nested Response DTO for Inspection Records
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InspectionRecordResponseDTO {
        private Long inspectionId;
        private Long productId;
        private String productName;
        private String productSku;
        private Integer orderedQuantity;
        private Integer receivedQuantity;
        private Integer acceptedQuantity;
        private Integer rejectedQuantity;
        private Integer damagedQuantity;
        private String inspectionStatus;
        private Integer qualityRating;
        private String defectType;
        private String batchNumber;
        private LocalDate expiryDate;
        private String inspectorName;
        private LocalDateTime inspectionDate;
        private String inspectionNotes;
        private String actionTaken;
        private Boolean quantityMatch;
        private Integer discrepancy;
    }
}