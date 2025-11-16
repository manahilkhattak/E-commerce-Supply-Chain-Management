package com.ecommerce.supplychain.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchScheduleResponseDTO {

    private Long scheduleId;
    private Long shipmentId;
    private String scheduleType;
    private LocalDateTime scheduledDateTime;
    private LocalDateTime actualDateTime;
    private String driverName;
    private String vehicleNumber;
    private String dispatchStatus;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isOverdue;
}