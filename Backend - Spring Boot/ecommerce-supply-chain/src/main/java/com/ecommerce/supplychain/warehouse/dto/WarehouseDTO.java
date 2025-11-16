package com.ecommerce.supplychain.warehouse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating/updating warehouses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {

    @NotBlank(message = "Warehouse code is required")
    @Size(max = 20, message = "Warehouse code must not exceed 20 characters")
    private String warehouseCode;

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 255, message = "Warehouse name must not exceed 255 characters")
    private String warehouseName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    private String state;
    private String postalCode;

    @NotNull(message = "Total capacity is required")
    @DecimalMin(value = "0.01", message = "Total capacity must be greater than 0")
    private Double totalCapacitySqft;

    @Pattern(regexp = "AMBIENT|REFRIGERATED|FROZEN|CONTROLLED",
            message = "Temperature zone must be AMBIENT, REFRIGERATED, FROZEN, or CONTROLLED")
    private String temperatureZone;

    @Pattern(regexp = "MAIN|REGIONAL|DISTRIBUTION|CROSS_DOCK",
            message = "Warehouse type must be MAIN, REGIONAL, DISTRIBUTION, or CROSS_DOCK")
    private String warehouseType;

    private String managerName;
    private String contactPhone;
    private String contactEmail;
    private String operatingHours;
    private String notes;
    private Boolean isActive;
}
