package com.ecommerce.supplychain.order.controller;

import com.ecommerce.supplychain.order.dto.*;
import com.ecommerce.supplychain.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * API 1: Create new customer order
     * POST /api/orders
     *
     * This API integrates with ALL 15 processes:
     * - Validates products (Process 5)
     * - Checks inventory (Process 6)
     * - Assigns warehouse (Process 8)
     * - Creates pick list (Process 9)
     * - Schedules quality check (Process 10)
     * - And more...
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try {
            OrderResponseDTO response = orderService.createOrder(orderDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Order created successfully and integrated with all supply chain processes");
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
     * API 2: Update order status
     * PUT /api/orders/{orderId}/status
     *
     * This triggers updates across multiple processes based on status change
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateDTO statusUpdateDTO) {
        try {
            OrderResponseDTO response = orderService.updateOrderStatus(orderId, statusUpdateDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Order status updated successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    /**
     * Get order by ID
     * GET /api/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long orderId) {
        try {
            OrderResponseDTO response = orderService.getOrderById(orderId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get order by order number
     * GET /api/orders/number/{orderNumber}
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Map<String, Object>> getOrderByNumber(@PathVariable String orderNumber) {
        try {
            OrderResponseDTO response = orderService.getOrderByNumber(orderNumber);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get all orders
     * GET /api/orders
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", orders.size());
        response.put("data", orders);

        return ResponseEntity.ok(response);
    }

    /**
     * Get orders by customer
     * GET /api/orders/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Map<String, Object>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomerId(customerId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("customerId", customerId);
        response.put("count", orders.size());
        response.put("data", orders);

        return ResponseEntity.ok(response);
    }

    /**
     * Get orders by status
     * GET /api/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getOrdersByStatus(@PathVariable String status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status);
        response.put("count", orders.size());
        response.put("data", orders);

        return ResponseEntity.ok(response);
    }

    /**
     * Get active orders
     * GET /api/orders/active
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveOrders() {
        List<OrderResponseDTO> orders = orderService.getActiveOrders();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active orders (PENDING, CONFIRMED, PROCESSING)");
        response.put("count", orders.size());
        response.put("data", orders);

        return ResponseEntity.ok(response);
    }

    /**
     * Cancel order
     * PUT /api/orders/{orderId}/cancel
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long orderId) {
        try {
            OrderResponseDTO response = orderService.cancelOrder(orderId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Order cancelled successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    /**
     * Health check for orders service
     * GET /api/orders/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Customer Orders");
        health.put("timestamp", java.time.LocalDateTime.now());
        health.put("integratedProcesses", List.of(
                "Process 5: Catalog",
                "Process 6: Inventory",
                "Process 8: Warehouse",
                "Process 9: Picking",
                "Process 10: Quality",
                "Process 11: Shipment",
                "Process 12: Tracking",
                "Process 14: Returns"
        ));

        return ResponseEntity.ok(health);
    }
}