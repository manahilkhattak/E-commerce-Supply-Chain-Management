package com.ecommerce.supplychain.receiving.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing Quality Inspection results for received goods.
 * Tracks item-level inspection details and quality metrics.
 */
@Entity
@Table(name = "inspection_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_id")
    private Long inspectionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private GoodsReceipt goodsReceipt;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", length = 50)
    private String productSku;

    @Column(name = "ordered_quantity", nullable = false)
    private Integer orderedQuantity;

    @Column(name = "received_quantity", nullable = false)
    private Integer receivedQuantity;

    @Column(name = "accepted_quantity")
    private Integer acceptedQuantity;

    @Column(name = "rejected_quantity")
    private Integer rejectedQuantity;

    @Column(name = "damaged_quantity")
    private Integer damagedQuantity;

    @Column(name = "inspection_status", length = 50)
    private String inspectionStatus; // PENDING, IN_PROGRESS, PASSED, FAILED, CONDITIONAL

    @Column(name = "quality_rating")
    private Integer qualityRating; // 1-5 scale

    @Column(name = "defect_type", length = 100)
    private String defectType; // DAMAGED, EXPIRED, WRONG_ITEM, POOR_QUALITY, PACKAGING_ISSUE

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(name = "expiry_date")
    private java.time.LocalDate expiryDate;

    @Column(name = "inspector_name", nullable = false, length = 100)
    private String inspectorName;

    @Column(name = "inspection_date")
    private LocalDateTime inspectionDate;

    @Column(name = "inspection_notes", columnDefinition = "TEXT")
    private String inspectionNotes;

    @Column(name = "photo_url", length = 500)
    private String photoUrl; // URL to inspection photos

    @Column(name = "action_taken", length = 100)
    private String actionTaken; // ACCEPT_ALL, PARTIAL_ACCEPT, REJECT_ALL, RETURN_TO_SUPPLIER

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (inspectionStatus == null) {
            inspectionStatus = "PENDING";
        }
        if (inspectionDate == null) {
            inspectionDate = LocalDateTime.now();
        }
        if (acceptedQuantity == null) {
            acceptedQuantity = 0;
        }
        if (rejectedQuantity == null) {
            rejectedQuantity = 0;
        }
        if (damagedQuantity == null) {
            damagedQuantity = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}