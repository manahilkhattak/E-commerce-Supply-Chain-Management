package com.ecommerce.supplychain.warehouse.repository;

import com.ecommerce.supplychain.warehouse.model.ShelfLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ShelfLocation entity.
 */
@Repository
public interface ShelfLocationRepository extends JpaRepository<ShelfLocation, Long> {

    Optional<ShelfLocation> findByLocationCode(String locationCode);

    List<ShelfLocation> findByWarehouse_WarehouseId(Long warehouseId);

    List<ShelfLocation> findByStorageZone_ZoneId(Long zoneId);

    List<ShelfLocation> findByLocationType(String locationType);

    List<ShelfLocation> findByLocationStatus(String locationStatus);

    List<ShelfLocation> findByIsOccupied(Boolean isOccupied);

    List<ShelfLocation> findByProductId(Long productId);

    boolean existsByLocationCode(String locationCode);

    @Query("SELECT sl FROM ShelfLocation sl WHERE sl.availableUnits >= :minUnits AND sl.locationStatus = 'AVAILABLE'")
    List<ShelfLocation> findAvailableShelvesWithCapacity(@Param("minUnits") Integer minUnits);

    @Query("SELECT sl FROM ShelfLocation sl WHERE sl.warehouse.warehouseId = :warehouseId AND sl.availableUnits >= :units")
    List<ShelfLocation> findAvailableShelvesInWarehouse(@Param("warehouseId") Long warehouseId, @Param("units") Integer units);

    @Query("SELECT sl FROM ShelfLocation sl WHERE sl.productId = :productId AND sl.currentUnits > 0")
    List<ShelfLocation> findShelvesWithProduct(@Param("productId") Long productId);

    @Query("SELECT sl FROM ShelfLocation sl WHERE sl.pickFrequency >= :minFrequency")
    List<ShelfLocation> findHighFrequencyShelves(@Param("minFrequency") Integer minFrequency);

    @Query("SELECT sl FROM ShelfLocation sl WHERE sl.occupancyRate <= :maxOccupancy AND sl.locationStatus = 'AVAILABLE'")
    List<ShelfLocation> findLowOccupancyShelves(@Param("maxOccupancy") Double maxOccupancy);
}