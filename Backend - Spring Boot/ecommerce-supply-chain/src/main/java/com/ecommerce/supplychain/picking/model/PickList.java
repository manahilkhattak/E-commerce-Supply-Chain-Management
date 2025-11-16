package com.ecommerce.supplychain.picking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a pick list for order fulfillment.
 * Integrates with Warehouse (Process 8) and Inventory (Process 6) for stock locations.
 */
@Entity
@Table(name = "pick_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_list_id")
    private Long pickListId;

    @Column(name = "pick_list_number", unique = true, nullable = false, length = 50)
    private String pickListNumber;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // Reference to Customer Order

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId; // Reference to Warehouse from Process 8

    @Column(name = "warehouse_name", length = 255)
    private String warehouseName;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo; // Picker username

    @Column(name = "priority_level", length = 20)
    private String priorityLevel; // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "pick_status", length = 50)
    private String pickStatus; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED, PARTIALLY_PICKED

    @Column(name = "total_items")
    private Integer totalItems;

    @Column(name = "picked_items")
    private Integer pickedItems;

    @Column(name = "remaining_items")
    private Integer remainingItems;

    @Column(name = "estimated_pick_time_minutes")
    private Integer estimatedPickTimeMinutes;

    @Column(name = "actual_pick_time_minutes")
    private Integer actualPickTimeMinutes;

    @Column(name = "pick_route_optimized")
    private Boolean pickRouteOptimized;

    @Column(name = "zone_sequence", length = 255)
    private String zoneSequence; // Optimized picking route

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "pick_notes", columnDefinition = "TEXT")
    private String pickNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pickList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickListItem> pickListItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (pickStatus == null) {
            pickStatus = "PENDING";
        }
        if (priorityLevel == null) {
            priorityLevel = "MEDIUM";
        }
        if (pickedItems == null) {
            pickedItems = 0;
        }
        if (remainingItems == null) {
            remainingItems = totalItems;
        }
        if (pickRouteOptimized == null) {
            pickRouteOptimized = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Update remaining items
        if (totalItems != null && pickedItems != null) {
            remainingItems = totalItems - pickedItems;
        }

        // Auto-update status based on progress
        if ("IN_PROGRESS".equals(pickStatus) && remainingItems == 0) {
            pickStatus = "COMPLETED";
            completedAt = LocalDateTime.now();
        }
    }

    /**
     * Start picking process
     */
    public void startPicking(String picker) {
        this.assignedTo = picker;
        this.pickStatus = "IN_PROGRESS";
        this.startedAt = LocalDateTime.now();
    }

    /**
     * Add picked item
     */
    public void addPickedItem() {
        this.pickedItems += 1;
        if (this.pickedItems.equals(this.totalItems)) {
            this.pickStatus = "COMPLETED";
            this.completedAt = LocalDateTime.now();
        } else if (this.pickedItems > 0) {
            this.pickStatus = "PARTIALLY_PICKED";
        }
    }

    /**
     * Calculate pick efficiency
     */
    public Double calculateEfficiency() {
        if (estimatedPickTimeMinutes != null && actualPickTimeMinutes != null && actualPickTimeMinutes > 0) {
            return (double) estimatedPickTimeMinutes / actualPickTimeMinutes * 100;
        }
        return 0.0;
    }
}

