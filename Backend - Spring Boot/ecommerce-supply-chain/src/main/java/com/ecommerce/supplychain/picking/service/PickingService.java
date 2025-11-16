package com.ecommerce.supplychain.picking.service;

import com.ecommerce.supplychain.picking.dto.*;
import com.ecommerce.supplychain.picking.model.PickList;
import com.ecommerce.supplychain.picking.model.PickListItem;
import com.ecommerce.supplychain.picking.model.Package;
import com.ecommerce.supplychain.picking.model.PackageItem;
import com.ecommerce.supplychain.picking.repository.PickListRepository;
import com.ecommerce.supplychain.picking.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling order picking and packaging business logic.
 */
@Service
public class PickingService {

    @Autowired
    private PickListRepository pickListRepository;

    @Autowired
    private PackageRepository packageRepository;

    /**
     * API 1: Create pick list for order
     */
    @Transactional
    public PickListResponseDTO createPickList(PickListDTO pickListDTO) {
        // Generate unique pick list number
        String pickListNumber = generatePickListNumber();

        // Create pick list entity
        PickList pickList = new PickList();
        pickList.setPickListNumber(pickListNumber);
        pickList.setOrderId(pickListDTO.getOrderId());
        pickList.setOrderNumber(pickListDTO.getOrderNumber());
        pickList.setWarehouseId(pickListDTO.getWarehouseId());
        pickList.setAssignedTo(pickListDTO.getAssignedTo());
        pickList.setPriorityLevel(pickListDTO.getPriorityLevel());
        pickList.setPickStatus("PENDING");
        pickList.setTotalItems(pickListDTO.getItems().size());
        pickList.setPickedItems(0);
        pickList.setRemainingItems(pickListDTO.getItems().size());
        pickList.setEstimatedPickTimeMinutes(calculateEstimatedPickTime(pickListDTO.getItems()));
        pickList.setPickRouteOptimized(optimizePickRoute(pickListDTO.getItems()));
        pickList.setZoneSequence(generateZoneSequence(pickListDTO.getItems()));
        pickList.setCreatedAt(LocalDateTime.now());
        pickList.setUpdatedAt(LocalDateTime.now());

        // Create pick list items
        for (int i = 0; i < pickListDTO.getItems().size(); i++) {
            PickListDTO.PickListItemDTO itemDTO = pickListDTO.getItems().get(i);

            PickListItem item = new PickListItem();
            item.setPickList(pickList);
            item.setProductId(itemDTO.getProductId());
            item.setProductName(itemDTO.getProductName());
            item.setProductSku(itemDTO.getProductSku());
            item.setShelfLocationId(itemDTO.getShelfLocationId());
            item.setLocationCode(itemDTO.getLocationCode());
            item.setRequiredQuantity(itemDTO.getRequiredQuantity());
            item.setPickedQuantity(0);
            item.setIsPicked(false);
            item.setPickSequence(itemDTO.getPickSequence() != null ? itemDTO.getPickSequence() : i + 1);
            item.setZoneCode(itemDTO.getZoneCode());
            item.setAisleNumber(itemDTO.getAisleNumber());
            item.setWeightPerUnitKg(itemDTO.getWeightPerUnitKg());

            pickList.getPickListItems().add(item);
        }

        PickList savedPickList = pickListRepository.save(pickList);

        return mapToPickListResponseDTO(savedPickList);
    }

    /**
     * API 2: Create package for shipment
     */
    @Transactional
    public PackageResponseDTO createPackage(PackageDTO packageDTO) {
        // Validate tracking number uniqueness
        if (packageRepository.existsByTrackingNumber(packageDTO.getTrackingNumber())) {
            throw new IllegalArgumentException("Package with tracking number " + packageDTO.getTrackingNumber() + " already exists");
        }

        // Verify pick list exists and is completed
        PickList pickList = pickListRepository.findById(packageDTO.getPickListId())
                .orElseThrow(() -> new IllegalArgumentException("Pick list not found with ID: " + packageDTO.getPickListId()));

        if (!"COMPLETED".equals(pickList.getPickStatus())) {
            throw new IllegalStateException("Cannot create package for incomplete pick list. Current status: " + pickList.getPickStatus());
        }

        // Create package entity
        Package packageEntity = new Package();
        packageEntity.setTrackingNumber(packageDTO.getTrackingNumber());
        packageEntity.setOrderId(packageDTO.getOrderId());
        packageEntity.setOrderNumber(packageDTO.getOrderNumber());
        packageEntity.setPickListId(packageDTO.getPickListId());
        packageEntity.setWarehouseId(packageDTO.getWarehouseId());
        packageEntity.setPackageType(packageDTO.getPackageType());
        packageEntity.setPackageSize(packageDTO.getPackageSize());
        packageEntity.setDimensions(packageDTO.getDimensions());
        packageEntity.setPackageStatus("PACKING");
        packageEntity.setCarrier(packageDTO.getCarrier());
        packageEntity.setServiceType(packageDTO.getServiceType());
        packageEntity.setShippingCost(packageDTO.getShippingCost());
        packageEntity.setInsuranceAmount(packageDTO.getInsuranceAmount());
        packageEntity.setRequiresSignature(packageDTO.getRequiresSignature());
        packageEntity.setIsFragile(packageDTO.getIsFragile());
        packageEntity.setIsHazardous(packageDTO.getIsHazardous());
        packageEntity.setTemperatureControl(packageDTO.getTemperatureControl());
        packageEntity.setCustomsDeclarationRequired(packageDTO.getCustomsDeclarationRequired());
        packageEntity.setPackageNotes(packageDTO.getPackageNotes());
        packageEntity.setCreatedAt(LocalDateTime.now());
        packageEntity.setUpdatedAt(LocalDateTime.now());

        // Create package items
        for (PackageDTO.PackageItemDTO itemDTO : packageDTO.getItems()) {
            PackageItem item = new PackageItem();
            item.setPackageEntity(packageEntity);
            item.setProductId(itemDTO.getProductId());
            item.setProductName(itemDTO.getProductName());
            item.setProductSku(itemDTO.getProductSku());
            item.setQuantity(itemDTO.getQuantity());
            item.setWeightKg(itemDTO.getWeightKg());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setIsFragile(itemDTO.getIsFragile());
            item.setRequiresSpecialHandling(itemDTO.getRequiresSpecialHandling());
            item.setItemNotes(itemDTO.getItemNotes());

            packageEntity.addPackageItem(item);
        }

        // Calculate total weight
        packageEntity.calculateTotalWeight();

        Package savedPackage = packageRepository.save(packageEntity);

        return mapToPackageResponseDTO(savedPackage);
    }

    /**
     * Update pick item status
     */
    @Transactional
    public PickListResponseDTO updatePickItem(PickItemUpdateDTO updateDTO) {
        // In a real implementation, this would update individual pick items
        // For now, we'll simulate the update

        PickList pickList = pickListRepository.findById(1L) // This would come from the pick item
                .orElseThrow(() -> new IllegalArgumentException("Pick list not found"));

        pickList.addPickedItem();
        pickList.setUpdatedAt(LocalDateTime.now());

        PickList updatedPickList = pickListRepository.save(pickList);

        return mapToPickListResponseDTO(updatedPickList);
    }

    /**
     * Mark package as packed
     */
    @Transactional
    public PackageResponseDTO markPackageAsPacked(Long packageId, String packedBy) {
        Package packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + packageId));

        packageEntity.markAsPacked(packedBy);

        Package updatedPackage = packageRepository.save(packageEntity);

        return mapToPackageResponseDTO(updatedPackage);
    }

    /**
     * Get all pick lists
     */
    public List<PickListResponseDTO> getAllPickLists() {
        return pickListRepository.findAll().stream()
                .map(this::mapToPickListResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get pick list by ID
     */
    public PickListResponseDTO getPickListById(Long pickListId) {
        PickList pickList = pickListRepository.findById(pickListId)
                .orElseThrow(() -> new IllegalArgumentException("Pick list not found with ID: " + pickListId));
        return mapToPickListResponseDTO(pickList);
    }

    /**
     * Get pick lists by order
     */
    public List<PickListResponseDTO> getPickListsByOrderId(Long orderId) {
        return pickListRepository.findByOrderId(orderId).stream()
                .map(this::mapToPickListResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active pick lists
     */
    public List<PickListResponseDTO> getActivePickLists() {
        return pickListRepository.findActivePickLists().stream()
                .map(this::mapToPickListResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all packages
     */
    public List<PackageResponseDTO> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(this::mapToPackageResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get package by ID
     */
    public PackageResponseDTO getPackageById(Long packageId) {
        Package packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + packageId));
        return mapToPackageResponseDTO(packageEntity);
    }

    /**
     * Get packages by order
     */
    public List<PackageResponseDTO> getPackagesByOrderId(Long orderId) {
        return packageRepository.findByOrderId(orderId).stream()
                .map(this::mapToPackageResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get packages ready for shipment
     */
    public List<PackageResponseDTO> getPackagesReadyForShipment() {
        return packageRepository.findPackagesReadyForShipment().stream()
                .map(this::mapToPackageResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Generate unique pick list number
     */
    private String generatePickListNumber() {
        return "PL-" + System.currentTimeMillis();
    }

    /**
     * Calculate estimated pick time based on items and locations
     */
    private Integer calculateEstimatedPickTime(List<PickListDTO.PickListItemDTO> items) {
        // Simplified calculation: 2 minutes per item + travel time
        return items.size() * 2 + 10;
    }

    /**
     * Optimize pick route based on zone sequence
     */
    private Boolean optimizePickRoute(List<PickListDTO.PickListItemDTO> items) {
        // Simplified optimization logic
        return items.size() > 3;
    }

    /**
     * Generate zone sequence for optimized picking
     */
    private String generateZoneSequence(List<PickListDTO.PickListItemDTO> items) {
        return items.stream()
                .map(item -> item.getZoneCode())
                .distinct()
                .collect(Collectors.joining(" → "));
    }

    /**
     * Helper method to convert PickList to ResponseDTO
     */
    private PickListResponseDTO mapToPickListResponseDTO(PickList pickList) {
        Double pickEfficiency = pickList.calculateEfficiency();
        Boolean isCompleted = "COMPLETED".equals(pickList.getPickStatus());

        List<PickListResponseDTO.PickListItemResponseDTO> itemDTOs = pickList.getPickListItems().stream()
                .map(item -> PickListResponseDTO.PickListItemResponseDTO.builder()
                        .pickItemId(item.getPickItemId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productSku(item.getProductSku())
                        .shelfLocationId(item.getShelfLocationId())
                        .locationCode(item.getLocationCode())
                        .requiredQuantity(item.getRequiredQuantity())
                        .pickedQuantity(item.getPickedQuantity())
                        .isPicked(item.getIsPicked())
                        .pickSequence(item.getPickSequence())
                        .zoneCode(item.getZoneCode())
                        .aisleNumber(item.getAisleNumber())
                        .weightPerUnitKg(item.getWeightPerUnitKg())
                        .pickNotes(item.getPickNotes())
                        .pickedAt(item.getPickedAt())
                        .fullLocationPath(item.getZoneCode() + "-" + item.getAisleNumber() + "-" + item.getLocationCode())
                        .build())
                .collect(Collectors.toList());

        return PickListResponseDTO.builder()
                .pickListId(pickList.getPickListId())
                .pickListNumber(pickList.getPickListNumber())
                .orderId(pickList.getOrderId())
                .orderNumber(pickList.getOrderNumber())
                .warehouseId(pickList.getWarehouseId())
                .warehouseName(pickList.getWarehouseName())
                .assignedTo(pickList.getAssignedTo())
                .priorityLevel(pickList.getPriorityLevel())
                .pickStatus(pickList.getPickStatus())
                .totalItems(pickList.getTotalItems())
                .pickedItems(pickList.getPickedItems())
                .remainingItems(pickList.getRemainingItems())
                .estimatedPickTimeMinutes(pickList.getEstimatedPickTimeMinutes())
                .actualPickTimeMinutes(pickList.getActualPickTimeMinutes())
                .pickRouteOptimized(pickList.getPickRouteOptimized())
                .zoneSequence(pickList.getZoneSequence())
                .startedAt(pickList.getStartedAt())
                .completedAt(pickList.getCompletedAt())
                .pickNotes(pickList.getPickNotes())
                .createdAt(pickList.getCreatedAt())
                .updatedAt(pickList.getUpdatedAt())
                .pickEfficiency(pickEfficiency)
                .isCompleted(isCompleted)
                .items(itemDTOs)
                .build();
    }

    /**
     * Helper method to convert Package to ResponseDTO
     */
    private PackageResponseDTO mapToPackageResponseDTO(Package packageEntity) {
        Integer totalItems = packageEntity.getPackageItems().size();
        Boolean isReadyForShipment = "READY_FOR_SHIPMENT".equals(packageEntity.getPackageStatus());

        List<PackageResponseDTO.PackageItemResponseDTO> itemDTOs = packageEntity.getPackageItems().stream()
                .map(item -> {
                    BigDecimal totalWeight = item.getWeightKg().multiply(BigDecimal.valueOf(item.getQuantity()));

                    return PackageResponseDTO.PackageItemResponseDTO.builder()
                            .packageItemId(item.getPackageItemId())
                            .productId(item.getProductId())
                            .productName(item.getProductName())
                            .productSku(item.getProductSku())
                            .quantity(item.getQuantity())
                            .weightKg(item.getWeightKg())
                            .unitPrice(item.getUnitPrice())
                            .isFragile(item.getIsFragile())
                            .requiresSpecialHandling(item.getRequiresSpecialHandling())
                            .itemNotes(item.getItemNotes())
                            .totalWeight(totalWeight)
                            .build();
                })
                .collect(Collectors.toList());

        return PackageResponseDTO.builder()
                .packageId(packageEntity.getPackageId())
                .trackingNumber(packageEntity.getTrackingNumber())
                .orderId(packageEntity.getOrderId())
                .orderNumber(packageEntity.getOrderNumber())
                .pickListId(packageEntity.getPickListId())
                .warehouseId(packageEntity.getWarehouseId())
                .packageType(packageEntity.getPackageType())
                .packageSize(packageEntity.getPackageSize())
                .weightKg(packageEntity.getWeightKg())
                .dimensions(packageEntity.getDimensions())
                .packageStatus(packageEntity.getPackageStatus())
                .packedBy(packageEntity.getPackedBy())
                .packedAt(packageEntity.getPackedAt())
                .carrier(packageEntity.getCarrier())
                .serviceType(packageEntity.getServiceType())
                .shippingCost(packageEntity.getShippingCost())
                .insuranceAmount(packageEntity.getInsuranceAmount())
                .requiresSignature(packageEntity.getRequiresSignature())
                .isFragile(packageEntity.getIsFragile())
                .isHazardous(packageEntity.getIsHazardous())
                .temperatureControl(packageEntity.getTemperatureControl())
                .customsDeclarationRequired(packageEntity.getCustomsDeclarationRequired())
                .packageNotes(packageEntity.getPackageNotes())
                .createdAt(packageEntity.getCreatedAt())
                .updatedAt(packageEntity.getUpdatedAt())
                .totalItems(totalItems)
                .isReadyForShipment(isReadyForShipment)
                .items(itemDTOs)
                .build();
    }
}