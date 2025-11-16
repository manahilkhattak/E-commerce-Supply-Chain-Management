package com.ecommerce.supplychain.picking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a package for shipment after picking.
 * Integrates with picking process and prepares for shipment (Process 11).
 */
@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "tracking_number", unique = true, nullable = false, length = 100)
    private String trackingNumber;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "pick_list_id", nullable = false)
    private Long pickListId; // Reference to PickList

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "package_type", length = 50)
    private String packageType; // BOX, ENVELOPE, PALLET, CARTON

    @Column(name = "package_size", length = 50)
    private String packageSize; // SMALL, MEDIUM, LARGE, EXTRA_LARGE

    @Column(name = "weight_kg", precision = 8, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "dimensions", length = 100)
    private String dimensions; // "LxWxH" in cm

    @Column(name = "package_status", length = 50)
    private String packageStatus; // PACKING, PACKED, LABELED, READY_FOR_SHIPMENT, SHIPPED

    @Column(name = "packed_by", length = 100)
    private String packedBy;

    @Column(name = "packed_at")
    private LocalDateTime packedAt;

    @Column(name = "carrier", length = 100)
    private String carrier; // UPS, FEDEX, DHL, USPS

    @Column(name = "service_type", length = 100)
    private String serviceType; // STANDARD, EXPRESS, OVERNIGHT

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "insurance_amount", precision = 10, scale = 2)
    private BigDecimal insuranceAmount;

    @Column(name = "requires_signature")
    private Boolean requiresSignature;

    @Column(name = "is_fragile")
    private Boolean isFragile;

    @Column(name = "is_hazardous")
    private Boolean isHazardous;

    @Column(name = "temperature_control", length = 50)
    private String temperatureControl; // AMBIENT, REFRIGERATED, FROZEN

    @Column(name = "customs_declaration_required")
    private Boolean customsDeclarationRequired;

    @Column(name = "package_notes", columnDefinition = "TEXT")
    private String packageNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "packageEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageItem> packageItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (packageStatus == null) {
            packageStatus = "PACKING";
        }
        if (packageType == null) {
            packageType = "BOX";
        }
        if (packageSize == null) {
            packageSize = "MEDIUM";
        }
        if (requiresSignature == null) {
            requiresSignature = false;
        }
        if (isFragile == null) {
            isFragile = false;
        }
        if (isHazardous == null) {
            isHazardous = false;
        }
        if (customsDeclarationRequired == null) {
            customsDeclarationRequired = false;
        }
        if (temperatureControl == null) {
            temperatureControl = "AMBIENT";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Mark package as packed
     */
    public void markAsPacked(String packedByUser) {
        this.packageStatus = "PACKED";
        this.packedBy = packedByUser;
        this.packedAt = LocalDateTime.now();
    }

    /**
     * Mark package as ready for shipment
     */
    public void markAsReadyForShipment() {
        this.packageStatus = "READY_FOR_SHIPMENT";
    }

    /**
     * Calculate total weight from items
     */
    public void calculateTotalWeight() {
        if (packageItems != null && !packageItems.isEmpty()) {
            BigDecimal totalWeight = packageItems.stream()
                    .map(item -> item.getWeightKg().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            this.weightKg = totalWeight;
        }
    }

    /**
     * Add item to package
     */
    public void addPackageItem(PackageItem item) {
        packageItems.add(item);
        item.setPackageEntity(this);
        calculateTotalWeight();
    }
}

