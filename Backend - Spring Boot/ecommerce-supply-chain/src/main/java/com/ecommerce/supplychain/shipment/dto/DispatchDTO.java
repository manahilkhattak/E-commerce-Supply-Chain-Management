package com.ecommerce.supplychain.shipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchDTO {

    @NotNull(message = "Shipment ID is required")
    private Long shipmentId;

    @NotBlank(message = "Schedule type is required")
    private String scheduleType;

    @NotNull(message = "Scheduled date time is required")
    private LocalDateTime scheduledDateTime;

    private String driverName;
    private String vehicleNumber;
    private String notes;
}