package com.ecommerce.supplychain.inventory.service;

import com.ecommerce.supplychain.inventory.dto.*;
import com.ecommerce.supplychain.inventory.model.Inventory;
import com.ecommerce.supplychain.inventory.model.StockAlert;
import com.ecommerce.supplychain.inventory.repository.InventoryRepository;
import com.ecommerce.supplychain.inventory.repository.StockAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling inventory monitoring and stock alert business logic.
 */
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StockAlertRepository stockAlertRepository;

    /**
     * API 1: Add product to inventory monitoring
     */
    @Transactional
    public InventoryResponseDTO addToInventoryMonitoring(InventoryDTO inventoryDTO) {
        // Check if product is already being monitored
        if (inventoryRepository.findByProductId(inventoryDTO.getProductId()).isPresent()) {
            throw new IllegalArgumentException("Product is already being monitored in inventory");
        }

        // Create inventory monitoring entity
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setProductName(inventoryDTO.getProductName());
        inventory.setProductSku(inventoryDTO.getProductSku());
        inventory.setCurrentStock(inventoryDTO.getCurrentStock());
        inventory.setMinimumStockLevel(inventoryDTO.getMinimumStockLevel());
        inventory.setMaximumStockLevel(inventoryDTO.getMaximumStockLevel());
        inventory.setReorderPoint(inventoryDTO.getReorderPoint());
        inventory.setStockValue(inventoryDTO.getStockValue());
        inventory.setStockTurnoverRate(inventoryDTO.getStockTurnoverRate());
        inventory.setDaysOfSupply(inventoryDTO.getDaysOfSupply());
        inventory.setIsMonitored(inventoryDTO.getIsMonitored() != null ? inventoryDTO.getIsMonitored() : true);
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());

        // Set initial movement frequency
        inventory.setMovementFrequency("LOW");

        Inventory savedInventory = inventoryRepository.save(inventory);

        // Check and create alerts if needed
        checkAndCreateAlerts(savedInventory);

        return mapToInventoryResponseDTO(savedInventory);
    }

    /**
     * API 2: Resolve stock alert
     */
    @Transactional
    public StockAlertResponseDTO resolveStockAlert(StockAlertDTO alertDTO) {
        StockAlert alert = stockAlertRepository.findById(alertDTO.getAlertId())
                .orElseThrow(() -> new IllegalArgumentException("Stock alert not found with ID: " + alertDTO.getAlertId()));

        if (alert.getIsResolved()) {
            throw new IllegalStateException("Alert is already resolved");
        }

        alert.resolveAlert(alertDTO.getResolvedBy(), alertDTO.getResolutionNotes());

        StockAlert resolvedAlert = stockAlertRepository.save(alert);

        return mapToStockAlertResponseDTO(resolvedAlert);
    }

    /**
     * Update inventory stock (called from sales, returns, etc.)
     */
    @Transactional
    public InventoryResponseDTO updateInventoryStock(StockUpdateDTO stockUpdateDTO) {
        Inventory inventory = inventoryRepository.findByProductId(stockUpdateDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found in inventory monitoring: " + stockUpdateDTO.getProductId()));

        int previousStock = inventory.getCurrentStock();

        switch (stockUpdateDTO.getUpdateType().toUpperCase()) {
            case "SALE":
                if (!inventory.sellStock(stockUpdateDTO.getQuantity())) {
                    throw new IllegalArgumentException("Insufficient stock for sale. Available: " + inventory.getAvailableStock());
                }
                break;

            case "RETURN":
                inventory.setCurrentStock(inventory.getCurrentStock() + stockUpdateDTO.getQuantity());
                break;

            case "ADJUSTMENT":
                inventory.setCurrentStock(stockUpdateDTO.getQuantity());
                break;

            case "RESERVE":
                if (!inventory.reserveStock(stockUpdateDTO.getQuantity())) {
                    throw new IllegalArgumentException("Cannot reserve stock. Available: " + inventory.getAvailableStock());
                }
                break;

            case "RELEASE":
                inventory.releaseReservedStock(stockUpdateDTO.getQuantity());
                break;

            default:
                throw new IllegalArgumentException("Invalid update type: " + stockUpdateDTO.getUpdateType());
        }

        inventory.setUpdatedAt(LocalDateTime.now());

        // Update last sold date for sales
        if ("SALE".equals(stockUpdateDTO.getUpdateType())) {
            inventory.setLastSoldDate(LocalDateTime.now());
        }

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Check and create alerts after stock update
        checkAndCreateAlerts(updatedInventory);

        return mapToInventoryResponseDTO(updatedInventory);
    }

    /**
     * Get all inventory items
     */
    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToInventoryResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get inventory by product ID
     */
    public InventoryResponseDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product ID: " + productId));
        return mapToInventoryResponseDTO(inventory);
    }

    /**
     * Get low stock items
     */
    public List<InventoryResponseDTO> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
                .map(this::mapToInventoryResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get out of stock items
     */
    public List<InventoryResponseDTO> getOutOfStockItems() {
        return inventoryRepository.findOutOfStockItems().stream()
                .map(this::mapToInventoryResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get overstock items
     */
    public List<InventoryResponseDTO> getOverstockItems() {
        return inventoryRepository.findOverstockItems().stream()
                .map(this::mapToInventoryResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all active stock alerts
     */
    public List<StockAlertResponseDTO> getActiveAlerts() {
        return stockAlertRepository.findByIsResolved(false).stream()
                .map(this::mapToStockAlertResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get critical unresolved alerts
     */
    public List<StockAlertResponseDTO> getCriticalAlerts() {
        return stockAlertRepository.findCriticalUnresolvedAlerts().stream()
                .map(this::mapToStockAlertResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get alerts by product
     */
    public List<StockAlertResponseDTO> getAlertsByProductId(Long productId) {
        return stockAlertRepository.findByProductId(productId).stream()
                .map(this::mapToStockAlertResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Run inventory health check and generate alerts
     */
    @Transactional
    public List<StockAlertResponseDTO> runInventoryHealthCheck() {
        List<Inventory> allInventory = inventoryRepository.findByIsMonitored(true);
        List<StockAlert> newAlerts = allInventory.stream()
                .map(this::checkAndCreateAlerts)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return newAlerts.stream()
                .map(this::mapToStockAlertResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check inventory and create alerts if needed
     */
    private List<StockAlert> checkAndCreateAlerts(Inventory inventory) {
        List<StockAlert> alerts = new java.util.ArrayList<>();

        // Check for low stock alert
        if (inventory.getCurrentStock() <= inventory.getReorderPoint() && inventory.getCurrentStock() > 0) {
            if (!hasActiveAlert(inventory, "LOW_STOCK")) {
                alerts.add(createStockAlert(inventory, "LOW_STOCK", "MEDIUM", inventory.getReorderPoint()));
            }
        }

        // Check for out of stock alert
        if (inventory.getCurrentStock() == 0) {
            if (!hasActiveAlert(inventory, "OUT_OF_STOCK")) {
                alerts.add(createStockAlert(inventory, "OUT_OF_STOCK", "CRITICAL", 0));
            }
        }

        // Check for overstock alert
        if (inventory.getMaximumStockLevel() != null &&
                inventory.getCurrentStock() > inventory.getMaximumStockLevel() * 0.9) {
            if (!hasActiveAlert(inventory, "OVERSTOCK")) {
                alerts.add(createStockAlert(inventory, "OVERSTOCK", "LOW", inventory.getMaximumStockLevel()));
            }
        }

        // Check for critical stock (below minimum)
        if (inventory.getCurrentStock() < inventory.getMinimumStockLevel() && inventory.getCurrentStock() > 0) {
            if (!hasActiveAlert(inventory, "LOW_STOCK")) {
                alerts.add(createStockAlert(inventory, "LOW_STOCK", "HIGH", inventory.getMinimumStockLevel()));
            }
        }

        stockAlertRepository.saveAll(alerts);
        return alerts;
    }

    /**
     * Check if product already has an active alert of given type
     */
    private boolean hasActiveAlert(Inventory inventory, String alertType) {
        return stockAlertRepository.findActiveAlertsByProduct(inventory.getProductId()).stream()
                .anyMatch(alert -> alertType.equals(alert.getAlertType()) && !alert.getIsResolved());
    }

    /**
     * Create stock alert
     */
    private StockAlert createStockAlert(Inventory inventory, String alertType, String alertLevel, Integer threshold) {
        StockAlert alert = new StockAlert();
        alert.setInventoryId(inventory.getInventoryId());
        alert.setProductId(inventory.getProductId());
        alert.setProductName(inventory.getProductName());
        alert.setProductSku(inventory.getProductSku());
        alert.setAlertType(alertType);
        alert.setAlertLevel(alertLevel);
        alert.setCurrentStock(inventory.getCurrentStock());
        alert.setThresholdStock(threshold);
        alert.setIsResolved(false);
        alert.setNotificationSent(false);
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());

        return alert;
    }

    /**
     * Helper method to convert Inventory to ResponseDTO
     */
    private InventoryResponseDTO mapToInventoryResponseDTO(Inventory inventory) {
        boolean needsAttention = !"OPTIMAL".equals(inventory.getStockStatus());
        int stockDeficit = Math.max(0, inventory.getMinimumStockLevel() - inventory.getCurrentStock());
        double stockCoverage = inventory.getDaysOfSupply() != null ? inventory.getDaysOfSupply() : 30.0;

        return InventoryResponseDTO.builder()
                .inventoryId(inventory.getInventoryId())
                .productId(inventory.getProductId())
                .productName(inventory.getProductName())
                .productSku(inventory.getProductSku())
                .currentStock(inventory.getCurrentStock())
                .reservedStock(inventory.getReservedStock())
                .availableStock(inventory.getAvailableStock())
                .minimumStockLevel(inventory.getMinimumStockLevel())
                .maximumStockLevel(inventory.getMaximumStockLevel())
                .reorderPoint(inventory.getReorderPoint())
                .stockValue(inventory.getStockValue())
                .stockTurnoverRate(inventory.getStockTurnoverRate())
                .daysOfSupply(inventory.getDaysOfSupply())
                .stockStatus(inventory.getStockStatus())
                .lastRestockedDate(inventory.getLastRestockedDate())
                .lastSoldDate(inventory.getLastSoldDate())
                .movementFrequency(inventory.getMovementFrequency())
                .isMonitored(inventory.getIsMonitored())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .needsAttention(needsAttention)
                .stockDeficit(stockDeficit)
                .stockCoverage(stockCoverage)
                .build();
    }

    /**
     * Helper method to convert StockAlert to ResponseDTO
     */
    private StockAlertResponseDTO mapToStockAlertResponseDTO(StockAlert alert) {
        Long daysOpen = null;
        if (!alert.getIsResolved()) {
            daysOpen = ChronoUnit.DAYS.between(alert.getCreatedAt(), LocalDateTime.now());
        }

        return StockAlertResponseDTO.builder()
                .alertId(alert.getAlertId())
                .inventoryId(alert.getInventoryId())
                .productId(alert.getProductId())
                .productName(alert.getProductName())
                .productSku(alert.getProductSku())
                .alertType(alert.getAlertType())
                .alertLevel(alert.getAlertLevel())
                .currentStock(alert.getCurrentStock())
                .thresholdStock(alert.getThresholdStock())
                .message(alert.getMessage())
                .isResolved(alert.getIsResolved())
                .resolvedBy(alert.getResolvedBy())
                .resolutionNotes(alert.getResolutionNotes())
                .resolvedAt(alert.getResolvedAt())
                .suggestedAction(alert.getSuggestedAction())
                .notificationSent(alert.getNotificationSent())
                .createdAt(alert.getCreatedAt())
                .updatedAt(alert.getUpdatedAt())
                .daysOpen(daysOpen)
                .build();
    }
}