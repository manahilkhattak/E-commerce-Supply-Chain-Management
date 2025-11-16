package com.ecommerce.supplychain.receiving.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing Goods Receipt when items arrive from suppliers.
 * Links to Purchase Orders and tracks received quantities.
 */
@Entity
@Table(name = "goods_receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long receiptId;

    @Column(name = "receipt_number", unique = true, nullable = false, length = 50)
    private String receiptNumber;

    @Column(name = "po_id", nullable = false)
    private Long poId; // Reference to Purchase Order

    @Column(name = "po_number", length = 50)
    private String poNumber;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "received_by", nullable = false, length = 100)
    private String receivedBy;

    @Column(name = "warehouse_location", length = 100)
    private String warehouseLocation;

    @Column(name = "delivery_note_number", length = 50)
    private String deliveryNoteNumber;

    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;

    @Column(name = "status", length = 50)
    private String status; // PENDING, RECEIVED, INSPECTED, ACCEPTED, REJECTED, PARTIALLY_ACCEPTED

    @Column(name = "total_items_ordered")
    private Integer totalItemsOrdered;

    @Column(name = "total_items_received")
    private Integer totalItemsReceived;

    @Column(name = "discrepancy_found")
    private Boolean discrepancyFound;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "goodsReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionRecord> inspectionRecords = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
        if (receiptDate == null) {
            receiptDate = LocalDate.now();
        }
        if (discrepancyFound == null) {
            discrepancyFound = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Helper method to add inspection record
     */
    public void addInspectionRecord(InspectionRecord record) {
        inspectionRecords.add(record);
        record.setGoodsReceipt(this);
    }
}
