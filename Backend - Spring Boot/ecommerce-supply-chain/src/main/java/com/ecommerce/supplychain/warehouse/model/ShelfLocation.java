package com.ecommerce.supplychain.warehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing specific shelf locations within warehouse zones.
 * Tracks exact storage positions for inventory items.
 */
@Entity
@Table(name = "shelf_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelf_id")
    private Long shelfId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private StorageZone storageZone;

    @Column(name = "location_code", unique = true, nullable = false, length = 50)
    private String locationCode; // Format: ZONE-AISLE-SHELF-LEVEL e.g., "A-01-B-02"

    @Column(name = "aisle_number", nullable = false, length = 10)
    private String aisleNumber;

    @Column(name = "shelf_number", nullable = false, length = 10)
    private String shelfNumber;

    @Column(name = "level_number", nullable = false, length = 10)
    private String levelNumber;

    @Column(name = "bin_number", length = 10)
    private String binNumber;

    @Column(name = "location_type", length = 50)
    private String locationType; // PICKING, STORAGE, BULK, RESERVE, RETURNS

    @Column(name = "max_capacity_units")
    private Integer maxCapacityUnits;

    @Column(name = "current_units")
    private Integer currentUnits;

    @Column(name = "available_units")
    private Integer availableUnits;

    @Column(name = "max_weight_kg")
    private Double maxWeightKg;

    @Column(name = "current_weight_kg")
    private Double currentWeightKg;

    @Column(name = "dimensions", length = 100)
    private String dimensions; // "LxWxH" in cm

    @Column(name = "temperature_requirement", length = 50)
    private String temperatureRequirement;

    @Column(name = "is_occupied")
    private Boolean isOccupied;

    @Column(name = "occupancy_rate")
    private Double occupancyRate;

    @Column(name = "product_id")
    private Long productId; // Currently stored product

    @Column(name = "product_name", length = 255)
    private String productName;

    @Column(name = "product_sku", length = 50)
    private String productSku;

    @Column(name = "last_restocked")
    private LocalDateTime lastRestocked;

    @Column(name = "last_picked")
    private LocalDateTime lastPicked;

    @Column(name = "pick_frequency")
    private Integer pickFrequency; // Times picked in last 30 days

    @Column(name = "location_status", length = 50)
    private String locationStatus; // AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE, BLOCKED

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (currentUnits == null) {
            currentUnits = 0;
        }
        if (availableUnits == null) {
            availableUnits = maxCapacityUnits;
        }
        if (currentWeightKg == null) {
            currentWeightKg = 0.0;
        }
        if (isOccupied == null) {
            isOccupied = false;
        }
        if (locationStatus == null) {
            locationStatus = "AVAILABLE";
        }
        if (locationType == null) {
            locationType = "STORAGE";
        }
        if (pickFrequency == null) {
            pickFrequency = 0;
        }

        calculateOccupancyRate();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateOccupancyRate();
        updateOccupancyStatus();
    }

    /**
     * Calculate occupancy rate percentage
     */
    private void calculateOccupancyRate() {
        if (maxCapacityUnits != null && maxCapacityUnits > 0) {
            this.occupancyRate = ((double) currentUnits / maxCapacityUnits) * 100;
        } else {
            this.occupancyRate = 0.0;
        }
    }

    /**
     * Update occupancy status based on current units
     */
    private void updateOccupancyStatus() {
        this.isOccupied = currentUnits > 0;
        if (currentUnits > 0) {
            this.locationStatus = "OCCUPIED";
        } else {
            this.locationStatus = "AVAILABLE";
        }
    }

    /**
     * Add units to shelf location
     */
    public boolean addUnits(Integer units, Double weight, Long productId, String productName, String productSku) {
        if (availableUnits >= units) {
            this.currentUnits += units;
            this.availableUnits = this.maxCapacityUnits - this.currentUnits;
            this.currentWeightKg += weight;
            this.productId = productId;
            this.productName = productName;
            this.productSku = productSku;
            this.lastRestocked = LocalDateTime.now();
            this.isOccupied = true;
            this.locationStatus = "OCCUPIED";
            calculateOccupancyRate();
            return true;
        }
        return false;
    }

    /**
     * Remove units from shelf location
     */
    public boolean removeUnits(Integer units, Double weight) {
        if (currentUnits >= units) {
            this.currentUnits -= units;
            this.availableUnits = this.maxCapacityUnits - this.currentUnits;
            this.currentWeightKg = Math.max(0, this.currentWeightKg - weight);
            this.lastPicked = LocalDateTime.now();
            this.pickFrequency += 1;

            if (this.currentUnits == 0) {
                this.productId = null;
                this.productName = null;
                this.productSku = null;
                this.isOccupied = false;
                this.locationStatus = "AVAILABLE";
            }

            calculateOccupancyRate();
            return true;
        }
        return false;
    }

    /**
     * Check if shelf has available capacity
     */
    public boolean hasAvailableCapacity(Integer requiredUnits) {
        return availableUnits >= requiredUnits;
    }

    /**
     * Get full location path
     */
    public String getFullLocationPath() {
        if (storageZone != null) {
            return storageZone.getZoneCode() + "-" + aisleNumber + "-" + shelfNumber + "-" + levelNumber;
        }
        return aisleNumber + "-" + shelfNumber + "-" + levelNumber;
    }
}