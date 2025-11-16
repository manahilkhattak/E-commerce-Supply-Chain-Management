package com.ecommerce.supplychain.warehouse.controller;

import com.ecommerce.supplychain.warehouse.dto.*;
import com.ecommerce.supplychain.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Warehouse Storage and Shelf Placement Management APIs.
 */
@RestController
@RequestMapping("/api/warehouse")
@CrossOrigin(origins = "*")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * API 1: Create new warehouse
     * POST /api/warehouse/warehouses
     */
    @PostMapping("/warehouses")
    public ResponseEntity<Map<String, Object>> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO) {
        try {
            WarehouseResponseDTO response = warehouseService.createWarehouse(warehouseDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Warehouse created successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * API 2: Place product on shelf location
     * POST /api/warehouse/shelf-placement
     */
    @PostMapping("/shelf-placement")
    public ResponseEntity<Map<String, Object>> placeProductOnShelf(@Valid @RequestBody ShelfPlacementDTO placementDTO) {
        try {
            ShelfLocationResponseDTO response = warehouseService.placeProductOnShelf(placementDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Product placed on shelf successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all warehouses
     * GET /api/warehouse/warehouses
     */
    @GetMapping("/warehouses")
    public ResponseEntity<Map<String, Object>> getAllWarehouses() {
        List<WarehouseResponseDTO> warehouses = warehouseService.getAllWarehouses();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", warehouses.size());
        response.put("data", warehouses);

        return ResponseEntity.ok(response);
    }

    /**
     * Get warehouse by ID
     * GET /api/warehouse/warehouses/{warehouseId}
     */
    @GetMapping("/warehouses/{warehouseId}")
    public ResponseEntity<Map<String, Object>> getWarehouseById(@PathVariable Long warehouseId) {
        try {
            WarehouseResponseDTO warehouse = warehouseService.getWarehouseById(warehouseId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", warehouse);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get shelf location by ID
     * GET /api/warehouse/shelves/{shelfId}
     */
    @GetMapping("/shelves/{shelfId}")
    public ResponseEntity<Map<String, Object>> getShelfLocationById(@PathVariable Long shelfId) {
        try {
            ShelfLocationResponseDTO shelf = warehouseService.getShelfLocationById(shelfId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", shelf);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get shelf locations by warehouse
     * GET /api/warehouse/warehouses/{warehouseId}/shelves
     */
    @GetMapping("/warehouses/{warehouseId}/shelves")
    public ResponseEntity<Map<String, Object>> getShelfLocationsByWarehouse(@PathVariable Long warehouseId) {
        List<ShelfLocationResponseDTO> shelves = warehouseService.getShelfLocationsByWarehouse(warehouseId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("warehouseId", warehouseId);
        response.put("count", shelves.size());
        response.put("data", shelves);

        return ResponseEntity.ok(response);
    }

    /**
     * Get available shelves with capacity
     * GET /api/warehouse/shelves/available
     */
    @GetMapping("/shelves/available")
    public ResponseEntity<Map<String, Object>> getAvailableShelvesWithCapacity(
            @RequestParam(defaultValue = "1") Integer minUnits) {
        List<ShelfLocationResponseDTO> availableShelves = warehouseService.getAvailableShelvesWithCapacity(minUnits);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Available shelves with capacity >= " + minUnits);
        response.put("count", availableShelves.size());
        response.put("data", availableShelves);

        return ResponseEntity.ok(response);
    }

    /**
     * Get shelves containing specific product
     * GET /api/warehouse/shelves/product/{productId}
     */
    @GetMapping("/shelves/product/{productId}")
    public ResponseEntity<Map<String, Object>> getShelvesWithProduct(@PathVariable Long productId) {
        List<ShelfLocationResponseDTO> shelves = warehouseService.getShelvesWithProduct(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("productId", productId);
        response.put("count", shelves.size());
        response.put("data", shelves);

        return ResponseEntity.ok(response);
    }

    /**
     * Remove product from shelf
     * PUT /api/warehouse/shelves/{shelfId}/remove
     */
    @PutMapping("/shelves/{shelfId}/remove")
    public ResponseEntity<Map<String, Object>> removeProductFromShelf(
            @PathVariable Long shelfId,
            @RequestParam Integer quantity,
            @RequestParam Double unitWeight) {
        try {
            ShelfLocationResponseDTO response = warehouseService.removeProductFromShelf(shelfId, quantity, unitWeight);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Product removed from shelf successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get warehouses with available capacity
     * GET /api/warehouse/warehouses/available-capacity
     */
    @GetMapping("/warehouses/available-capacity")
    public ResponseEntity<Map<String, Object>> getWarehousesWithAvailableCapacity(
            @RequestParam(defaultValue = "100.0") Double minCapacity) {
        List<WarehouseResponseDTO> warehouses = warehouseService.getWarehousesWithAvailableCapacity(minCapacity);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Warehouses with available capacity >= " + minCapacity + " sqft");
        response.put("count", warehouses.size());
        response.put("data", warehouses);

        return ResponseEntity.ok(response);
    }
}