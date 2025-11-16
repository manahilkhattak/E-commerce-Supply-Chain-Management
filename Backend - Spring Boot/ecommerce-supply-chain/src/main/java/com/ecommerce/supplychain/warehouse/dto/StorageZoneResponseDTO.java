package com.ecommerce.supplychain.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for storage zone information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageZoneResponseDTO {

    private Long zoneId;
    private Long warehouseId;
    private String warehouseName;
    private String zoneCode;
    private String zoneName;
    private String zoneType;
    private String temperatureControl;
    private Double totalCapacitySqft;
    private Double usedCapacitySqft;
    private Double availableCapacitySqft;
    private Double maxWeightCapacityKg;
    private Double currentWeightKg;
    private Integer aisleCount;
    private Integer shelfCount;
    private String accessRequirements;
    private String zoneStatus;
    private Double capacityUtilization;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer availableShelves;
    private String utilizationLevel; // LOW, MEDIUM, HIGH, FULL
}
