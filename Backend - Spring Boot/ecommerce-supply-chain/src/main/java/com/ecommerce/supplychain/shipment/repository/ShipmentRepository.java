package com.ecommerce.supplychain.shipment.repository;

import com.ecommerce.supplychain.shipment.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    List<Shipment> findByOrderId(Long orderId);

    List<Shipment> findByShipmentStatus(String shipmentStatus);

    List<Shipment> findByCarrier(String carrier);

    boolean existsByTrackingNumber(String trackingNumber);

    @Query("SELECT s FROM Shipment s WHERE s.shipmentDate BETWEEN :startDate AND :endDate")
    List<Shipment> findByShipmentDateBetween(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    @Query("SELECT s FROM Shipment s WHERE s.estimatedDeliveryDate < :today AND s.shipmentStatus != 'DELIVERED'")
    List<Shipment> findOverdueShipments(@Param("today") LocalDate today);

    @Query("SELECT s FROM Shipment s WHERE s.shipmentStatus = 'SCHEDULED' AND s.shipmentDate <= :today")
    List<Shipment> findReadyForDispatch(@Param("today") LocalDate today);

    List<Shipment> findByRecipientNameContainingIgnoreCase(String recipientName);
}