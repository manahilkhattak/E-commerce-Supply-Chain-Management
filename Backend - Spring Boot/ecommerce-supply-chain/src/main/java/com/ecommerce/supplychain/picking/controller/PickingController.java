package com.ecommerce.supplychain.picking.controller;

import com.ecommerce.supplychain.picking.dto.*;
import com.ecommerce.supplychain.picking.service.PickingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Order Picking and Packaging Management APIs.
 */
@RestController
@RequestMapping("/api/picking")
@CrossOrigin(origins = "*")
public class PickingController {

    @Autowired
    private PickingService pickingService;

    /**
     * API 1: Create pick list for order
     * POST /api/picking/pick-lists
     */
    @PostMapping("/pick-lists")
    public ResponseEntity<Map<String, Object>> createPickList(@Valid @RequestBody PickListDTO pickListDTO) {
        try {
            PickListResponseDTO response = pickingService.createPickList(pickListDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Pick list created successfully");
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
     * API 2: Create package for shipment
     * POST /api/picking/packages
     */
    @PostMapping("/packages")
    public ResponseEntity<Map<String, Object>> createPackage(@Valid @RequestBody PackageDTO packageDTO) {
        try {
            PackageResponseDTO response = pickingService.createPackage(packageDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Package created successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Update pick item status
     * PUT /api/picking/pick-items/update
     */
    @PutMapping("/pick-items/update")
    public ResponseEntity<Map<String, Object>> updatePickItem(@Valid @RequestBody PickItemUpdateDTO updateDTO) {
        try {
            PickListResponseDTO response = pickingService.updatePickItem(updateDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Pick item updated successfully");
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
     * Mark package as packed
     * PUT /api/picking/packages/{packageId}/mark-packed
     */
    @PutMapping("/packages/{packageId}/mark-packed")
    public ResponseEntity<Map<String, Object>> markPackageAsPacked(
            @PathVariable Long packageId,
            @RequestParam String packedBy) {
        try {
            PackageResponseDTO response = pickingService.markPackageAsPacked(packageId, packedBy);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Package marked as packed successfully");
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
     * Get all pick lists
     * GET /api/picking/pick-lists
     */
    @GetMapping("/pick-lists")
    public ResponseEntity<Map<String, Object>> getAllPickLists() {
        List<PickListResponseDTO> pickLists = pickingService.getAllPickLists();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", pickLists.size());
        response.put("data", pickLists);

        return ResponseEntity.ok(response);
    }

    /**
     * Get pick list by ID
     * GET /api/picking/pick-lists/{pickListId}
     */
    @GetMapping("/pick-lists/{pickListId}")
    public ResponseEntity<Map<String, Object>> getPickListById(@PathVariable Long pickListId) {
        try {
            PickListResponseDTO pickList = pickingService.getPickListById(pickListId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", pickList);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get pick lists by order
     * GET /api/picking/pick-lists/order/{orderId}
     */
    @GetMapping("/pick-lists/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getPickListsByOrderId(@PathVariable Long orderId) {
        List<PickListResponseDTO> pickLists = pickingService.getPickListsByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderId);
        response.put("count", pickLists.size());
        response.put("data", pickLists);

        return ResponseEntity.ok(response);
    }

    /**
     * Get active pick lists
     * GET /api/picking/pick-lists/active
     */
    @GetMapping("/pick-lists/active")
    public ResponseEntity<Map<String, Object>> getActivePickLists() {
        List<PickListResponseDTO> activePickLists = pickingService.getActivePickLists();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active pick lists (PENDING or IN_PROGRESS)");
        response.put("count", activePickLists.size());
        response.put("data", activePickLists);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all packages
     * GET /api/picking/packages
     */
    @GetMapping("/packages")
    public ResponseEntity<Map<String, Object>> getAllPackages() {
        List<PackageResponseDTO> packages = pickingService.getAllPackages();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", packages.size());
        response.put("data", packages);

        return ResponseEntity.ok(response);
    }

    /**
     * Get package by ID
     * GET /api/picking/packages/{packageId}
     */
    @GetMapping("/packages/{packageId}")
    public ResponseEntity<Map<String, Object>> getPackageById(@PathVariable Long packageId) {
        try {
            PackageResponseDTO packageEntity = pickingService.getPackageById(packageId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", packageEntity);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get packages by order
     * GET /api/picking/packages/order/{orderId}
     */
    @GetMapping("/packages/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getPackagesByOrderId(@PathVariable Long orderId) {
        List<PackageResponseDTO> packages = pickingService.getPackagesByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderId);
        response.put("count", packages.size());
        response.put("data", packages);

        return ResponseEntity.ok(response);
    }

    /**
     * Get packages ready for shipment
     * GET /api/picking/packages/ready-for-shipment
     */
    @GetMapping("/packages/ready-for-shipment")
    public ResponseEntity<Map<String, Object>> getPackagesReadyForShipment() {
        List<PackageResponseDTO> packages = pickingService.getPackagesReadyForShipment();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Packages ready for shipment");
        response.put("count", packages.size());
        response.put("data", packages);

        return ResponseEntity.ok(response);
    }
}