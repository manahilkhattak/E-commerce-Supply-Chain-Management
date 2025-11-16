package com.ecommerce.supplychain.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing stock alerts and notifications.
 * Automatically generated when inventory levels reach critical points.
 */
@Entity
@Table(name = "stock_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId; // Reference to Inventory

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType; // LOW_STOCK, OUT_OF_STOCK, OVERSTOCK, EXPIRING, SLOW_MOVING

    @Column(name = "alert_level", nullable = false, length = 20)
    private String alertLevel; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "current_stock")
    private Integer currentStock;

    @Column(name = "threshold_stock")
    private Integer thresholdStock;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "is_resolved")
    private Boolean isResolved;

    @Column(name = "resolved_by", length = 100)
    private String resolvedBy;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "suggested_action", columnDefinition = "TEXT")
    private String suggestedAction;

    @Column(name = "notification_sent")
    private Boolean notificationSent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (isResolved == null) {
            isResolved = false;
        }
        if (notificationSent == null) {
            notificationSent = false;
        }

        // Auto-generate message based on alert type
        if (message == null) {
            message = generateAlertMessage();
        }

        // Auto-suggest action
        if (suggestedAction == null) {
            suggestedAction = generateSuggestedAction();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateAlertMessage() {
        switch (alertType) {
            case "LOW_STOCK":
                return String.format("Low stock alert for %s (SKU: %s). Current stock: %d, Reorder point: %d",
                        productName, productSku, currentStock, thresholdStock);
            case "OUT_OF_STOCK":
                return String.format("Out of stock alert for %s (SKU: %s). Urgent restocking required.",
                        productName, productSku);
            case "OVERSTOCK":
                return String.format("Overstock alert for %s (SKU: %s). Current stock: %d, Maximum level: %d",
                        productName, productSku, currentStock, thresholdStock);
            case "SLOW_MOVING":
                return String.format("Slow moving product alert for %s (SKU: %s). Consider promotions.",
                        productName, productSku);
            default:
                return String.format("Stock alert for %s (SKU: %s)", productName, productSku);
        }
    }

    private String generateSuggestedAction() {
        switch (alertType) {
            case "LOW_STOCK":
                return "Place purchase order with supplier immediately.";
            case "OUT_OF_STOCK":
                return "Urgent: Contact supplier for emergency restocking. Consider alternative suppliers.";
            case "OVERSTOCK":
                return "Run promotions or discounts to reduce excess inventory.";
            case "SLOW_MOVING":
                return "Consider bundling, promotions, or reviewing pricing strategy.";
            default:
                return "Review inventory levels and take appropriate action.";
        }
    }

    /**
     * Resolve the alert
     */
    public void resolveAlert(String resolvedBy, String resolutionNotes) {
        this.isResolved = true;
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
        this.resolvedAt = LocalDateTime.now();
    }
}