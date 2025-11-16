package com.ecommerce.supplychain.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for warehouse information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponseDTO {

    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private Double totalCapacitySqft;
    private Double usedCapacitySqft;
    private Double availableCapacitySqft;
    private Integer totalShelves;
    private Integer occupiedShelves;
    private String temperatureZone;
    private String warehouseType;
    private Boolean isActive;
    private String managerName;
    private String contactPhone;
    private String contactEmail;
    private String operatingHours;
    private Double capacityUtilization;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totalZones;
    private Integer availableShelves;
    private String utilizationStatus; // OPTIMAL, HIGH, CRITICAL
}
