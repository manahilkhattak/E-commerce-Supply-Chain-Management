package com.ecommerce.supplychain.tracking.repository;

import com.ecommerce.supplychain.tracking.model.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {

    List<TrackingEvent> findByTrackingNumber(String trackingNumber);

    List<TrackingEvent> findByOrderId(Long orderId);

    List<TrackingEvent> findByShipmentId(Long shipmentId);

    List<TrackingEvent> findByPackageId(Long packageId);

    List<TrackingEvent> findByEventType(String eventType);

    List<TrackingEvent> findByCarrier(String carrier);

    Optional<TrackingEvent> findFirstByTrackingNumberOrderByEventTimestampDesc(String trackingNumber);

    @Query("SELECT te FROM TrackingEvent te WHERE te.eventTimestamp BETWEEN :startDate AND :endDate")
    List<TrackingEvent> findEventsByDateRange(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT te FROM TrackingEvent te WHERE te.trackingNumber = :trackingNumber AND te.isMilestone = true ORDER BY te.eventTimestamp")
    List<TrackingEvent> findMilestoneEventsByTrackingNumber(@Param("trackingNumber") String trackingNumber);

    @Query("SELECT te FROM TrackingEvent te WHERE te.orderId = :orderId ORDER BY te.eventTimestamp DESC")
    List<TrackingEvent> findOrderTrackingHistory(@Param("orderId") Long orderId);

    boolean existsByTrackingNumberAndEventType(String trackingNumber, String eventType);
}