package com.ecommerce.supplychain.tracking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryUpdateDTO {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PENDING|SHIPPED|IN_TRANSIT|OUT_FOR_DELIVERY|DELIVERED|EXCEPTION|RETURNED",
            message = "Status must be PENDING, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, EXCEPTION, or RETURNED")
    private String status;

    private String statusDescription;

    private String location;

    private String carrier;

    private String serviceType;

    private LocalDateTime estimatedDelivery;

    private LocalDateTime actualDelivery;

    private String signedBy;

    private Boolean isException;

    private String exceptionReason;

    private String exceptionResolution;

    private Boolean customerNotified;
}