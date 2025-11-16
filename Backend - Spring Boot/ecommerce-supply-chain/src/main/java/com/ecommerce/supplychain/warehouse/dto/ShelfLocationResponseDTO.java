package com.ecommerce.supplychain.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for shelf location information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelfLocationResponseDTO {

    private Long shelfId;
    private Long warehouseId;
    private String warehouseName;
    private Long zoneId;
    private String zoneCode;
    private String locationCode;
    private String aisleNumber;
    private String shelfNumber;
    private String levelNumber;
    private String binNumber;
    private String locationType;
    private Integer maxCapacityUnits;
    private Integer currentUnits;
    private Integer availableUnits;
    private Double maxWeightKg;
    private Double currentWeightKg;
    private String dimensions;
    private String temperatureRequirement;
    private Boolean isOccupied;
    private Double occupancyRate;
    private Long productId;
    private String productName;
    private String productSku;
    private LocalDateTime lastRestocked;
    private LocalDateTime lastPicked;
    private Integer pickFrequency;
    private String locationStatus;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String fullLocationPath;
    private Boolean hasAvailableCapacity;
    private Integer availableCapacity;
}
