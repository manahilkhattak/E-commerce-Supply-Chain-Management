package com.ecommerce.supplychain.picking.repository;

import com.ecommerce.supplychain.picking.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Package entity.
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    Optional<Package> findByTrackingNumber(String trackingNumber);

    List<Package> findByOrderId(Long orderId);

    List<Package> findByPickListId(Long pickListId);

    List<Package> findByWarehouseId(Long warehouseId);

    List<Package> findByPackageStatus(String packageStatus);

    List<Package> findByPackedBy(String packedBy);

    List<Package> findByCarrier(String carrier);

    boolean existsByTrackingNumber(String trackingNumber);

    @Query("SELECT p FROM Package p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Package> findPackagesByDateRange(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Package p WHERE p.packageStatus = 'READY_FOR_SHIPMENT'")
    List<Package> findPackagesReadyForShipment();

    @Query("SELECT p FROM Package p WHERE p.orderId = :orderId AND p.packageStatus != 'SHIPPED'")
    List<Package> findActivePackagesByOrder(@Param("orderId") Long orderId);

    @Query("SELECT p FROM Package p WHERE p.packedAt BETWEEN :startDate AND :endDate")
    List<Package> findPackagesPackedByDateRange(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}