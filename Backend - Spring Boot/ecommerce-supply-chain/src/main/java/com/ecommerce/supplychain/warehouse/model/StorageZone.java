package com.ecommerce.supplychain.warehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing storage zones within a warehouse.
 * Organizes warehouse space into logical zones for better management.
 */
@Entity
@Table(name = "storage_zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Long zoneId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "zone_code", nullable = false, length = 20)
    private String zoneCode;

    @Column(name = "zone_name", nullable = false, length = 255)
    private String zoneName;

    @Column(name = "zone_type", nullable = false, length = 50)
    private String zoneType; // BULK, PICKING, RESERVE, RETURNS, QUARANTINE, HAZARDOUS

    @Column(name = "temperature_control", length = 50)
    private String temperatureControl; // AMBIENT, REFRIGERATED, FROZEN, CONTROLLED

    @Column(name = "total_capacity_sqft", nullable = false)
    private Double totalCapacitySqft;

    @Column(name = "used_capacity_sqft")
    private Double usedCapacitySqft;

    @Column(name = "available_capacity_sqft")
    private Double availableCapacitySqft;

    @Column(name = "max_weight_capacity_kg")
    private Double maxWeightCapacityKg;

    @Column(name = "current_weight_kg")
    private Double currentWeightKg;

    @Column(name = "aisle_count")
    private Integer aisleCount;

    @Column(name = "shelf_count")
    private Integer shelfCount;

    @Column(name = "access_requirements", length = 100)
    private String accessRequirements; // STANDARD, RESTRICTED, AUTHORIZED_ONLY

    @Column(name = "zone_status", length = 50)
    private String zoneStatus; // ACTIVE, MAINTENANCE, FULL, CLOSED

    @Column(name = "capacity_utilization")
    private Double capacityUtilization;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "storageZone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShelfLocation> shelfLocations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (usedCapacitySqft == null) {
            usedCapacitySqft = 0.0;
        }
        if (availableCapacitySqft == null) {
            availableCapacitySqft = totalCapacitySqft;
        }
        if (currentWeightKg == null) {
            currentWeightKg = 0.0;
        }
        if (zoneStatus == null) {
            zoneStatus = "ACTIVE";
        }
        if (accessRequirements == null) {
            accessRequirements = "STANDARD";
        }

        calculateCapacityUtilization();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateCapacityUtilization();
    }

    /**
     * Calculate capacity utilization percentage
     */
    private void calculateCapacityUtilization() {
        if (totalCapacitySqft != null && totalCapacitySqft > 0) {
            this.capacityUtilization = (usedCapacitySqft / totalCapacitySqft) * 100;
        } else {
            this.capacityUtilization = 0.0;
        }
    }

    /**
     * Update zone capacity
     */
    public void updateCapacity(Double additionalUsedSpace, Double additionalWeight) {
        this.usedCapacitySqft += additionalUsedSpace;
        this.availableCapacitySqft = this.totalCapacitySqft - this.usedCapacitySqft;
        this.currentWeightKg += additionalWeight;
        calculateCapacityUtilization();
    }

    /**
     * Check if zone has available capacity
     */
    public boolean hasAvailableCapacity(Double requiredSpace) {
        return availableCapacitySqft >= requiredSpace;
    }

    /**
     * Add shelf location to zone
     */
    public void addShelfLocation(ShelfLocation shelf) {
        shelfLocations.add(shelf);
        shelf.setStorageZone(this);
        this.shelfCount = shelfLocations.size();
    }
}