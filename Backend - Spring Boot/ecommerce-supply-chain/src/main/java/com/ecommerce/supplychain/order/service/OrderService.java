package com.ecommerce.supplychain.order.service;

import com.ecommerce.supplychain.order.dto.*;
import com.ecommerce.supplychain.order.model.Order;
import com.ecommerce.supplychain.order.model.OrderItem;
import com.ecommerce.supplychain.order.repository.OrderRepository;
import com.ecommerce.supplychain.order.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * API 1: Create new customer order
     * This integrates with ALL processes:
     * - Process 5 (Catalog): Validates products
     * - Process 6 (Inventory): Checks stock availability
     * - Process 8 (Warehouse): Assigns warehouse
     * - Process 9 (Picking): Triggers pick list creation
     * - Process 10 (Quality): Triggers quality checks if needed
     */
    @Transactional
    public OrderResponseDTO createOrder(OrderDTO orderDTO) {
        // Generate unique order number
        String orderNumber = generateOrderNumber();

        // Create order entity
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setCustomerId(orderDTO.getCustomerId());
        order.setCustomerName(orderDTO.getCustomerName());
        order.setCustomerEmail(orderDTO.getCustomerEmail());
        order.setCustomerPhone(orderDTO.getCustomerPhone());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setBillingAddress(orderDTO.getBillingAddress() != null ?
                orderDTO.getBillingAddress() : orderDTO.getShippingAddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setShippingCost(orderDTO.getShippingCost() != null ?
                orderDTO.getShippingCost() : BigDecimal.ZERO);
        order.setTaxAmount(orderDTO.getTaxAmount() != null ?
                orderDTO.getTaxAmount() : BigDecimal.ZERO);
        order.setDiscountAmount(orderDTO.getDiscountAmount() != null ?
                orderDTO.getDiscountAmount() : BigDecimal.ZERO);
        order.setCurrency(orderDTO.getCurrency() != null ?
                orderDTO.getCurrency() : "USD");
        order.setWarehouseId(orderDTO.getWarehouseId());
        order.setPriorityLevel(orderDTO.getPriorityLevel());
        order.setOrderNotes(orderDTO.getOrderNotes());
        order.setOrderStatus("PENDING");
        order.setPaymentStatus("PENDING");

        // Create order items
        for (OrderDTO.OrderItemDTO itemDTO : orderDTO.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setProductName(itemDTO.getProductName());
            orderItem.setProductSku(itemDTO.getProductSku());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(itemDTO.getUnitPrice());
            orderItem.setWeightKg(itemDTO.getWeightKg());
            orderItem.setIsFragile(itemDTO.getIsFragile());
            orderItem.setRequiresQualityCheck(itemDTO.getRequiresQualityCheck());
            orderItem.setCategory(itemDTO.getCategory());
            orderItem.setBrand(itemDTO.getBrand());
            orderItem.setItemNotes(itemDTO.getItemNotes());

            order.getOrderItems().add(orderItem);
        }

        // Calculate totals
        order.calculateTotals();

        // Set estimated delivery (2-7 days from now)
        order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));

        // Save order
        Order savedOrder = orderRepository.save(order);

        // INTEGRATION WITH OTHER PROCESSES
        integrateWithOtherProcesses(savedOrder);

        return mapToOrderResponseDTO(savedOrder);
    }

    /**
     * API 2: Update order status
     * This triggers updates across multiple processes
     */
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusUpdateDTO statusUpdateDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        String oldStatus = order.getOrderStatus();
        String newStatus = statusUpdateDTO.getOrderStatus();

        order.setOrderStatus(newStatus);
        order.setInternalNotes(statusUpdateDTO.getInternalNotes());
        order.setUpdatedAt(LocalDateTime.now());

        // Handle status-specific logic
        handleStatusTransition(order, oldStatus, newStatus);

        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponseDTO(updatedOrder);
    }

    /**
     * Get order by ID
     */
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        return mapToOrderResponseDTO(order);
    }

    /**
     * Get order by order number
     */
    public OrderResponseDTO getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with number: " + orderNumber));
        return mapToOrderResponseDTO(order);
    }

    /**
     * Get all orders
     */
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get orders by customer
     */
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get orders by status
     */
    public List<OrderResponseDTO> getOrdersByStatus(String status) {
        return orderRepository.findByOrderStatus(status).stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active orders
     */
    public List<OrderResponseDTO> getActiveOrders() {
        return orderRepository.findActiveOrders().stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cancel order
     */
    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled. Current status: " + order.getOrderStatus());
        }

        order.setOrderStatus("CANCELLED");
        order.setPaymentStatus("REFUNDED");
        order.setUpdatedAt(LocalDateTime.now());

        Order cancelledOrder = orderRepository.save(order);

        // Trigger cancellation in other processes
        triggerCancellationInOtherProcesses(cancelledOrder);

        return mapToOrderResponseDTO(cancelledOrder);
    }

    /**
     * Generate unique order number
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    /**
     * Integrate with other processes when order is created
     */
    private void integrateWithOtherProcesses(Order order) {
        // Process 6 (Inventory): Check stock availability
        checkInventoryAvailability(order);

        // Process 8 (Warehouse): Assign warehouse if not specified
        assignOptimalWarehouse(order);

        // Process 9 (Picking): Create pick list
        createPickList(order);

        // Process 10 (Quality): Schedule quality check if needed
        if (order.requiresQualityCheck()) {
            scheduleQualityCheck(order);
        }

        // Update order with process references
        orderRepository.save(order);
    }

    /**
     * Handle status transitions and trigger process updates
     */
    private void handleStatusTransition(Order order, String oldStatus, String newStatus) {
        switch (newStatus) {
            case "CONFIRMED":
                // Process 11 (Shipment): Schedule shipment
                scheduleShipment(order);
                break;
            case "SHIPPED":
                // Process 12 (Tracking): Start tracking
                startTracking(order);
                order.setActualDeliveryDate(LocalDateTime.now().plusDays(2)); // Estimate delivery
                break;
            case "DELIVERED":
                // Process 12 (Tracking): Mark as delivered
                completeTracking(order);
                order.setActualDeliveryDate(LocalDateTime.now());
                break;
            case "CANCELLED":
                // Process 14 (Returns): Initiate return process
                initiateReturnProcess(order);
                break;
        }
    }

    /**
     * Integration with Process 6: Inventory Availability Check
     */
    private void checkInventoryAvailability(Order order) {
        // In real implementation, this would call Inventory Service
        for (OrderItem item : order.getOrderItems()) {
            // Simulate inventory check
            boolean inStock = true; // This would be actual check

            if (!inStock) {
                throw new IllegalStateException("Product " + item.getProductName() + " is out of stock");
            }

            // Reserve inventory
            item.setInventoryId(1000L + item.getProductId()); // Mock inventory ID
        }
    }

    /**
     * Integration with Process 8: Warehouse Assignment
     */
    private void assignOptimalWarehouse(Order order) {
        if (order.getWarehouseId() == null) {
            // Assign based on customer location and product availability
            Long optimalWarehouseId = 1L; // This would be calculated
            order.setWarehouseId(optimalWarehouseId);
        }

        // Assign shelf locations for each item
        for (OrderItem item : order.getOrderItems()) {
            item.setShelfLocationId(2000L + item.getProductId()); // Mock shelf location
        }
    }

    /**
     * Integration with Process 9: Pick List Creation
     */
    private void createPickList(Order order) {
        // This would call Picking Service to create pick list
        Long pickListId = 3000L + order.getOrderId(); // Mock pick list ID
        order.setPickListId(pickListId);
    }

    /**
     * Integration with Process 10: Quality Check Scheduling
     */
    private void scheduleQualityCheck(Order order) {
        // This would call Quality Service
        Long qualityCheckId = 4000L + order.getOrderId(); // Mock quality check ID
        order.setQualityCheckId(qualityCheckId);
    }

    /**
     * Integration with Process 11: Shipment Scheduling
     */
    private void scheduleShipment(Order order) {
        // This would call Shipment Service
        Long shipmentId = 5000L + order.getOrderId(); // Mock shipment ID
        order.setShipmentId(shipmentId);
    }

    /**
     * Integration with Process 12: Tracking
     */
    private void startTracking(Order order) {
        // This would call Tracking Service
        // Tracking would be automatically started when shipment is created
    }

    private void completeTracking(Order order) {
        // This would call Tracking Service to mark as delivered
    }

    /**
     * Integration with Process 14: Returns
     */
    private void initiateReturnProcess(Order order) {
        // This would call Returns Service for cancellation/return
    }

    /**
     * Trigger cancellation across all processes
     */
    private void triggerCancellationInOtherProcesses(Order order) {
        // Cancel pick list (Process 9)
        // Cancel shipment (Process 11)
        // Stop tracking (Process 12)
        // Restore inventory (Process 6)
    }

    /**
     * Map Order entity to Response DTO
     */
    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        List<OrderResponseDTO.OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream()
                .map(item -> OrderResponseDTO.OrderItemResponseDTO.builder()
                        .orderItemId(item.getOrderItemId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productSku(item.getProductSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .weightKg(item.getWeightKg())
                        .isFragile(item.getIsFragile())
                        .requiresQualityCheck(item.getRequiresQualityCheck())
                        .category(item.getCategory())
                        .brand(item.getBrand())
                        .itemNotes(item.getItemNotes())
                        .inventoryId(item.getInventoryId())
                        .shelfLocationId(item.getShelfLocationId())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .shippingCost(order.getShippingCost())
                .taxAmount(order.getTaxAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .currency(order.getCurrency())
                .warehouseId(order.getWarehouseId())
                .priorityLevel(order.getPriorityLevel())
                .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
                .actualDeliveryDate(order.getActualDeliveryDate())
                .orderNotes(order.getOrderNotes())
                .internalNotes(order.getInternalNotes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .pickListId(order.getPickListId())
                .shipmentId(order.getShipmentId())
                .qualityCheckId(order.getQualityCheckId())
                .canBeCancelled(order.canBeCancelled())
                .requiresQualityCheck(order.requiresQualityCheck())
                .totalItems(order.getOrderItems().size())
                .items(itemDTOs)
                .build();
    }
}