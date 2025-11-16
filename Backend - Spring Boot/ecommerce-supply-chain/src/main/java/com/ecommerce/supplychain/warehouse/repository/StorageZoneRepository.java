package com.ecommerce.supplychain.warehouse.repository;

import com.ecommerce.supplychain.warehouse.model.StorageZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for StorageZone entity.
 */
@Repository
public interface StorageZoneRepository extends JpaRepository<StorageZone, Long> {

    List<StorageZone> findByWarehouse_WarehouseId(Long warehouseId);

    List<StorageZone> findByZoneType(String zoneType);

    List<StorageZone> findByZoneStatus(String zoneStatus);

    List<StorageZone> findByTemperatureControl(String temperatureControl);

    @Query("SELECT sz FROM StorageZone sz WHERE sz.warehouse.warehouseId = :warehouseId AND sz.availableCapacitySqft >= :minCapacity")
    List<StorageZone> findAvailableZonesInWarehouse(@Param("warehouseId") Long warehouseId, @Param("minCapacity") Double minCapacity);

    @Query("SELECT sz FROM StorageZone sz WHERE sz.capacityUtilization <= :maxUtilization")
    List<StorageZone> findZonesWithAvailableCapacity(@Param("maxUtilization") Double maxUtilization);

    @Query("SELECT sz FROM StorageZone sz WHERE sz.zoneCode = :zoneCode AND sz.warehouse.warehouseId = :warehouseId")
    List<StorageZone> findByZoneCodeAndWarehouse(@Param("zoneCode") String zoneCode, @Param("warehouseId") Long warehouseId);
}