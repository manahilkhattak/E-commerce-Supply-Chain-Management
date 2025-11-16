package com.ecommerce.supplychain.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a physical warehouse facility.
 * Manages warehouse information, capacity, and storage zones.
 */
@Entity
@Table(name = "warehouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "warehouse_code", unique = true, nullable = false, length = 20)
    private String warehouseCode;

    @Column(name = "warehouse_name", nullable = false, length = 255)
    private String warehouseName;

    @Column(name = "address", columnDefinition = "TEXT", nullable = false)
    private String address;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "total_capacity_sqft", nullable = false)
    private Double totalCapacitySqft;

    @Column(name = "used_capacity_sqft")
    private Double usedCapacitySqft;

    @Column(name = "available_capacity_sqft")
    private Double availableCapacitySqft;

    @Column(name = "total_shelves")
    private Integer totalShelves;

    @Column(name = "occupied_shelves")
    private Integer occupiedShelves;

    @Column(name = "temperature_zone", length = 50)
    private String temperatureZone; // AMBIENT, REFRIGERATED, FROZEN, CONTROLLED

    @Column(name = "warehouse_type", length = 50)
    private String warehouseType; // MAIN, REGIONAL, DISTRIBUTION, CROSS_DOCK

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "manager_name", length = 100)
    private String managerName;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "operating_hours", length = 100)
    private String operatingHours;

    @Column(name = "capacity_utilization")
    private Double capacityUtilization;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageZone> storageZones = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
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
        if (totalShelves == null) {
            totalShelves = 0;
        }
        if (occupiedShelves == null) {
            occupiedShelves = 0;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (temperatureZone == null) {
            temperatureZone = "AMBIENT";
        }
        if (warehouseType == null) {
            warehouseType = "DISTRIBUTION";
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
     * Update capacity when storage changes
     */
    public void updateCapacity(Double additionalUsedSpace) {
        this.usedCapacitySqft += additionalUsedSpace;
        this.availableCapacitySqft = this.totalCapacitySqft - this.usedCapacitySqft;
        calculateCapacityUtilization();
    }

    /**
     * Add storage zone to warehouse
     */
    public void addStorageZone(StorageZone zone) {
        storageZones.add(zone);
        zone.setWarehouse(this);
    }

    /**
     * Add shelf location to warehouse
     */
    public void addShelfLocation(ShelfLocation shelf) {
        shelfLocations.add(shelf);
        shelf.setWarehouse(this);
        this.totalShelves = shelfLocations.size();
    }
}