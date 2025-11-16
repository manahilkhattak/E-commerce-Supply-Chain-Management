package com.ecommerce.supplychain.warehouse.repository;

import com.ecommerce.supplychain.warehouse.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Warehouse entity.
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    List<Warehouse> findByCity(String city);

    List<Warehouse> findByCountry(String country);

    List<Warehouse> findByWarehouseType(String warehouseType);

    List<Warehouse> findByTemperatureZone(String temperatureZone);

    List<Warehouse> findByIsActive(Boolean isActive);

    boolean existsByWarehouseCode(String warehouseCode);

    @Query("SELECT w FROM Warehouse w WHERE w.capacityUtilization >= :utilization")
    List<Warehouse> findByHighUtilization(@Param("utilization") Double utilization);

    @Query("SELECT w FROM Warehouse w WHERE w.availableCapacitySqft >= :minCapacity")
    List<Warehouse> findByAvailableCapacity(@Param("minCapacity") Double minCapacity);

    @Query("SELECT w FROM Warehouse w WHERE w.city = :city AND w.isActive = true")
    List<Warehouse> findActiveWarehousesByCity(@Param("city") String city);

    @Query("SELECT w FROM Warehouse w WHERE w.warehouseName LIKE %:searchTerm% OR w.warehouseCode LIKE %:searchTerm%")
    List<Warehouse> searchWarehouses(@Param("searchTerm") String searchTerm);
}