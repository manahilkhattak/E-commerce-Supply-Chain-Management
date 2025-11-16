package com.ecommerce.supplychain.shipment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "dispatch_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(name = "schedule_type", nullable = false, length = 50)
    private String scheduleType;

    @Column(name = "scheduled_date_time", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(name = "actual_date_time")
    private LocalDateTime actualDateTime;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;

    @Column(name = "dispatch_status", length = 50)
    private String dispatchStatus;

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
        if (dispatchStatus == null) {
            dispatchStatus = "SCHEDULED";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void markAsDispatched() {
        this.dispatchStatus = "DISPATCHED";
        this.actualDateTime = LocalDateTime.now();
    }

    public void markAsCompleted() {
        this.dispatchStatus = "COMPLETED";
    }
}