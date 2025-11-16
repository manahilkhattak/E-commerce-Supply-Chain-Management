package com.ecommerce.supplychain.exception.repository;

import com.ecommerce.supplychain.exception.model.DeliveryException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryExceptionRepository extends JpaRepository<DeliveryException, Long> {

    Optional<DeliveryException> findByExceptionNumber(String exceptionNumber);

    List<DeliveryException> findByTrackingNumber(String trackingNumber);

    List<DeliveryException> findByOrderId(Long orderId);

    List<DeliveryException> findByShipmentId(Long shipmentId);

    List<DeliveryException> findByExceptionType(String exceptionType);

    List<DeliveryException> findByExceptionStatus(String exceptionStatus);

    List<DeliveryException> findByAssignedTo(String assignedTo);

    List<DeliveryException> findByExceptionSeverity(String exceptionSeverity);

    List<DeliveryException> findByPriorityLevel(String priorityLevel);

    List<DeliveryException> findByCarrier(String carrier);

    boolean existsByTrackingNumberAndExceptionStatus(String trackingNumber, String exceptionStatus);

    @Query("SELECT de FROM DeliveryException de WHERE de.exceptionDate BETWEEN :startDate AND :endDate")
    List<DeliveryException> findExceptionsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT de FROM DeliveryException de WHERE de.exceptionStatus IN ('OPEN', 'IN_PROGRESS', 'ESCALATED')")
    List<DeliveryException> findActiveExceptions();

    @Query("SELECT de FROM DeliveryException de WHERE de.requiresInsuranceClaim = true AND de.insuranceClaimFiled = false")
    List<DeliveryException> findExceptionsRequiringInsuranceClaim();

    @Query("SELECT de FROM DeliveryException de WHERE de.customerContacted = false AND de.exceptionSeverity IN ('HIGH', 'CRITICAL')")
    List<DeliveryException> findCriticalExceptionsRequiringCustomerContact();

    @Query("SELECT COUNT(de) FROM DeliveryException de WHERE de.exceptionStatus = 'OPEN'")
    Long countOpenExceptions();

    @Query("SELECT de FROM DeliveryException de WHERE de.estimatedResolutionDate < :currentDate AND de.exceptionStatus != 'RESOLVED'")
    List<DeliveryException> findOverdueExceptions(@Param("currentDate") LocalDateTime currentDate);
}