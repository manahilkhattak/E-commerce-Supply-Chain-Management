package com.ecommerce.supplychain.warehouse.service;

import com.ecommerce.supplychain.warehouse.dto.*;
import com.ecommerce.supplychain.warehouse.model.ShelfLocation;
import com.ecommerce.supplychain.warehouse.model.StorageZone;
import com.ecommerce.supplychain.warehouse.model.Warehouse;
import com.ecommerce.supplychain.warehouse.repository.ShelfLocationRepository;
import com.ecommerce.supplychain.warehouse.repository.StorageZoneRepository;
import com.ecommerce.supplychain.warehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling warehouse and shelf management business logic.
 */
@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ShelfLocationRepository shelfLocationRepository;

    @Autowired
    private StorageZoneRepository storageZoneRepository;

    /**
     * API 1: Create new warehouse
     */
    @Transactional
    public WarehouseResponseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        // Validate warehouse code uniqueness
        if (warehouseRepository.existsByWarehouseCode(warehouseDTO.getWarehouseCode())) {
            throw new IllegalArgumentException("Warehouse with code " + warehouseDTO.getWarehouseCode() + " already exists");
        }

        // Create warehouse entity
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseCode(warehouseDTO.getWarehouseCode());
        warehouse.setWarehouseName(warehouseDTO.getWarehouseName());
        warehouse.setAddress(warehouseDTO.getAddress());
        warehouse.setCity(warehouseDTO.getCity());
        warehouse.setState(warehouseDTO.getState());
        warehouse.setCountry(warehouseDTO.getCountry());
        warehouse.setPostalCode(warehouseDTO.getPostalCode());
        warehouse.setTotalCapacitySqft(warehouseDTO.getTotalCapacitySqft());
        warehouse.setTemperatureZone(warehouseDTO.getTemperatureZone());
        warehouse.setWarehouseType(warehouseDTO.getWarehouseType());
        warehouse.setManagerName(warehouseDTO.getManagerName());
        warehouse.setContactPhone(warehouseDTO.getContactPhone());
        warehouse.setContactEmail(warehouseDTO.getContactEmail());
        warehouse.setOperatingHours(warehouseDTO.getOperatingHours());
        warehouse.setNotes(warehouseDTO.getNotes());
        warehouse.setIsActive(warehouseDTO.getIsActive() != null ? warehouseDTO.getIsActive() : true);
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        return mapToWarehouseResponseDTO(savedWarehouse);
    }

    /**
     * API 2: Place product on shelf location
     */
    @Transactional
    public ShelfLocationResponseDTO placeProductOnShelf(ShelfPlacementDTO placementDTO) {
        ShelfLocation shelf = shelfLocationRepository.findById(placementDTO.getShelfId())
                .orElseThrow(() -> new IllegalArgumentException("Shelf location not found with ID: " + placementDTO.getShelfId()));

        // Check if shelf has available capacity
        if (!shelf.hasAvailableCapacity(placementDTO.getQuantity())) {
            throw new IllegalArgumentException("Insufficient capacity on shelf. Available: " + shelf.getAvailableUnits() + ", Required: " + placementDTO.getQuantity());
        }

        // Calculate total weight
        Double totalWeight = placementDTO.getQuantity() * placementDTO.getUnitWeightKg();

        // Check weight capacity
        if (shelf.getMaxWeightKg() != null && (shelf.getCurrentWeightKg() + totalWeight) > shelf.getMaxWeightKg()) {
            throw new IllegalArgumentException("Weight capacity exceeded. Current: " + shelf.getCurrentWeightKg() + "kg, Additional: " + totalWeight + "kg, Max: " + shelf.getMaxWeightKg() + "kg");
        }

        // Add units to shelf
        boolean success = shelf.addUnits(
                placementDTO.getQuantity(),
                totalWeight,
                placementDTO.getProductId(),
                placementDTO.getProductName(),
                placementDTO.getProductSku()
        );

        if (!success) {
            throw new IllegalStateException("Failed to place product on shelf");
        }

        // Update warehouse and zone capacities
        updateParentCapacities(shelf, placementDTO.getQuantity(), totalWeight);

        ShelfLocation updatedShelf = shelfLocationRepository.save(shelf);

        return mapToShelfLocationResponseDTO(updatedShelf);
    }

    /**
     * Get all warehouses
     */
    public List<WarehouseResponseDTO> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::mapToWarehouseResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get warehouse by ID
     */
    public WarehouseResponseDTO getWarehouseById(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with ID: " + warehouseId));
        return mapToWarehouseResponseDTO(warehouse);
    }

    /**
     * Get shelf location by ID
     */
    public ShelfLocationResponseDTO getShelfLocationById(Long shelfId) {
        ShelfLocation shelf = shelfLocationRepository.findById(shelfId)
                .orElseThrow(() -> new IllegalArgumentException("Shelf location not found with ID: " + shelfId));
        return mapToShelfLocationResponseDTO(shelf);
    }

    /**
     * Get all shelf locations in warehouse
     */
    public List<ShelfLocationResponseDTO> getShelfLocationsByWarehouse(Long warehouseId) {
        return shelfLocationRepository.findByWarehouse_WarehouseId(warehouseId).stream()
                .map(this::mapToShelfLocationResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get available shelf locations with capacity
     */
    public List<ShelfLocationResponseDTO> getAvailableShelvesWithCapacity(Integer minUnits) {
        return shelfLocationRepository.findAvailableShelvesWithCapacity(minUnits).stream()
                .map(this::mapToShelfLocationResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get shelves containing specific product
     */
    public List<ShelfLocationResponseDTO> getShelvesWithProduct(Long productId) {
        return shelfLocationRepository.findShelvesWithProduct(productId).stream()
                .map(this::mapToShelfLocationResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create shelf location
     */
    @Transactional
    public ShelfLocationResponseDTO createShelfLocation(ShelfLocation shelfLocation) {
        // Validate location code uniqueness
        if (shelfLocationRepository.existsByLocationCode(shelfLocation.getLocationCode())) {
            throw new IllegalArgumentException("Shelf location with code " + shelfLocation.getLocationCode() + " already exists");
        }

        ShelfLocation savedShelf = shelfLocationRepository.save(shelfLocation);

        // Update warehouse shelf count
        Warehouse warehouse = savedShelf.getWarehouse();
        warehouse.setTotalShelves(warehouse.getShelfLocations().size());
        warehouseRepository.save(warehouse);

        return mapToShelfLocationResponseDTO(savedShelf);
    }

    /**
     * Remove product from shelf location
     */
    @Transactional
    public ShelfLocationResponseDTO removeProductFromShelf(Long shelfId, Integer quantity, Double unitWeight) {
        ShelfLocation shelf = shelfLocationRepository.findById(shelfId)
                .orElseThrow(() -> new IllegalArgumentException("Shelf location not found with ID: " + shelfId));

        Double totalWeight = quantity * unitWeight;

        boolean success = shelf.removeUnits(quantity, totalWeight);

        if (!success) {
            throw new IllegalArgumentException("Cannot remove more units than available. Available: " + shelf.getCurrentUnits());
        }

        // Update warehouse and zone capacities (negative values for removal)
        updateParentCapacities(shelf, -quantity, -totalWeight);

        ShelfLocation updatedShelf = shelfLocationRepository.save(shelf);

        return mapToShelfLocationResponseDTO(updatedShelf);
    }

    /**
     * Get warehouses with available capacity
     */
    public List<WarehouseResponseDTO> getWarehousesWithAvailableCapacity(Double minCapacity) {
        return warehouseRepository.findByAvailableCapacity(minCapacity).stream()
                .map(this::mapToWarehouseResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update parent capacities when shelf changes
     */
    private void updateParentCapacities(ShelfLocation shelf, Integer unitChange, Double weightChange) {
        // Estimate space change (simplified: 1 unit = 0.1 sqft)
        Double spaceChange = unitChange * 0.1;

        // Update warehouse capacity
        Warehouse warehouse = shelf.getWarehouse();
        warehouse.updateCapacity(spaceChange);
        warehouseRepository.save(warehouse);

        // Update zone capacity if applicable
        if (shelf.getStorageZone() != null) {
            StorageZone zone = shelf.getStorageZone();
            zone.updateCapacity(spaceChange, weightChange);
            storageZoneRepository.save(zone);
        }
    }

    /**
     * Helper method to convert Warehouse to ResponseDTO
     */
    private WarehouseResponseDTO mapToWarehouseResponseDTO(Warehouse warehouse) {
        int availableShelves = warehouse.getShelfLocations().stream()
                .filter(shelf -> !shelf.getIsOccupied())
                .mapToInt(shelf -> 1)
                .sum();

        String utilizationStatus;
        if (warehouse.getCapacityUtilization() >= 90) {
            utilizationStatus = "CRITICAL";
        } else if (warehouse.getCapacityUtilization() >= 75) {
            utilizationStatus = "HIGH";
        } else {
            utilizationStatus = "OPTIMAL";
        }

        return WarehouseResponseDTO.builder()
                .warehouseId(warehouse.getWarehouseId())
                .warehouseCode(warehouse.getWarehouseCode())
                .warehouseName(warehouse.getWarehouseName())
                .address(warehouse.getAddress())
                .city(warehouse.getCity())
                .state(warehouse.getState())
                .country(warehouse.getCountry())
                .postalCode(warehouse.getPostalCode())
                .totalCapacitySqft(warehouse.getTotalCapacitySqft())
                .usedCapacitySqft(warehouse.getUsedCapacitySqft())
                .availableCapacitySqft(warehouse.getAvailableCapacitySqft())
                .totalShelves(warehouse.getTotalShelves())
                .occupiedShelves(warehouse.getOccupiedShelves())
                .temperatureZone(warehouse.getTemperatureZone())
                .warehouseType(warehouse.getWarehouseType())
                .isActive(warehouse.getIsActive())
                .managerName(warehouse.getManagerName())
                .contactPhone(warehouse.getContactPhone())
                .contactEmail(warehouse.getContactEmail())
                .operatingHours(warehouse.getOperatingHours())
                .capacityUtilization(warehouse.getCapacityUtilization())
                .notes(warehouse.getNotes())
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .totalZones(warehouse.getStorageZones().size())
                .availableShelves(availableShelves)
                .utilizationStatus(utilizationStatus)
                .build();
    }

    /**
     * Helper method to convert ShelfLocation to ResponseDTO
     */
    private ShelfLocationResponseDTO mapToShelfLocationResponseDTO(ShelfLocation shelf) {
        return ShelfLocationResponseDTO.builder()
                .shelfId(shelf.getShelfId())
                .warehouseId(shelf.getWarehouse().getWarehouseId())
                .warehouseName(shelf.getWarehouse().getWarehouseName())
                .zoneId(shelf.getStorageZone() != null ? shelf.getStorageZone().getZoneId() : null)
                .zoneCode(shelf.getStorageZone() != null ? shelf.getStorageZone().getZoneCode() : null)
                .locationCode(shelf.getLocationCode())
                .aisleNumber(shelf.getAisleNumber())
                .shelfNumber(shelf.getShelfNumber())
                .levelNumber(shelf.getLevelNumber())
                .binNumber(shelf.getBinNumber())
                .locationType(shelf.getLocationType())
                .maxCapacityUnits(shelf.getMaxCapacityUnits())
                .currentUnits(shelf.getCurrentUnits())
                .availableUnits(shelf.getAvailableUnits())
                .maxWeightKg(shelf.getMaxWeightKg())
                .currentWeightKg(shelf.getCurrentWeightKg())
                .dimensions(shelf.getDimensions())
                .temperatureRequirement(shelf.getTemperatureRequirement())
                .isOccupied(shelf.getIsOccupied())
                .occupancyRate(shelf.getOccupancyRate())
                .productId(shelf.getProductId())
                .productName(shelf.getProductName())
                .productSku(shelf.getProductSku())
                .lastRestocked(shelf.getLastRestocked())
                .lastPicked(shelf.getLastPicked())
                .pickFrequency(shelf.getPickFrequency())
                .locationStatus(shelf.getLocationStatus())
                .notes(shelf.getNotes())
                .createdAt(shelf.getCreatedAt())
                .updatedAt(shelf.getUpdatedAt())
                .fullLocationPath(shelf.getFullLocationPath())
                .hasAvailableCapacity(shelf.getAvailableUnits() > 0)
                .availableCapacity(shelf.getAvailableUnits())
                .build();
    }
}