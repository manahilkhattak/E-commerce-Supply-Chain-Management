package com.ecommerce.supplychain.procurement.controller;

import com.ecommerce.supplychain.procurement.dto.PurchaseOrderDTO;
import com.ecommerce.supplychain.procurement.dto.PurchaseOrderResponseDTO;
import com.ecommerce.supplychain.procurement.service.ProcurementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Procurement and Purchase Order Management APIs.
 */
@RestController
@RequestMapping("/api/procurement")
@CrossOrigin(origins = "*")
public class ProcurementController {

    @Autowired
    private ProcurementService procurementService;

    /**
     * API 1: Create a new purchase order
     * POST /api/procurement/purchase-orders
     */
    @PostMapping("/purchase-orders")
    public ResponseEntity<Map<String, Object>> createPurchaseOrder(@Valid @RequestBody PurchaseOrderDTO dto) {
        try {
            PurchaseOrderResponseDTO response = procurementService.createPurchaseOrder(dto);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Purchase order created successfully");
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
     * API 2: Approve a purchase order
     * PUT /api/procurement/purchase-orders/{poId}/approve
     */
    @PutMapping("/purchase-orders/{poId}/approve")
    public ResponseEntity<Map<String, Object>> approvePurchaseOrder(
            @PathVariable Long poId,
            @RequestParam String approvedBy) {
        try {
            PurchaseOrderResponseDTO response = procurementService.approvePurchaseOrder(poId, approvedBy);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Purchase order approved successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all purchase orders
     * GET /api/procurement/purchase-orders
     */
    @GetMapping("/purchase-orders")
    public ResponseEntity<Map<String, Object>> getAllPurchaseOrders() {
        List<PurchaseOrderResponseDTO> purchaseOrders = procurementService.getAllPurchaseOrders();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", purchaseOrders.size());
        response.put("data", purchaseOrders);

        return ResponseEntity.ok(response);
    }

    /**
     * Get purchase order by ID
     * GET /api/procurement/purchase-orders/{poId}
     */
    @GetMapping("/purchase-orders/{poId}")
    public ResponseEntity<Map<String, Object>> getPurchaseOrderById(@PathVariable Long poId) {
        try {
            PurchaseOrderResponseDTO po = procurementService.getPurchaseOrderById(poId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", po);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get purchase orders by supplier
     * GET /api/procurement/purchase-orders/supplier/{supplierId}
     */
    @GetMapping("/purchase-orders/supplier/{supplierId}")
    public ResponseEntity<Map<String, Object>> getPurchaseOrdersBySupplierId(@PathVariable Long supplierId) {
        List<PurchaseOrderResponseDTO> purchaseOrders = procurementService.getPurchaseOrdersBySupplierId(supplierId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("supplierId", supplierId);
        response.put("count", purchaseOrders.size());
        response.put("data", purchaseOrders);

        return ResponseEntity.ok(response);
    }

    /**
     * Get purchase orders by status
     * GET /api/procurement/purchase-orders/status/{status}
     */
    @GetMapping("/purchase-orders/status/{status}")
    public ResponseEntity<Map<String, Object>> getPurchaseOrdersByStatus(@PathVariable String status) {
        List<PurchaseOrderResponseDTO> purchaseOrders = procurementService.getPurchaseOrdersByStatus(status.toUpperCase());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status.toUpperCase());
        response.put("count", purchaseOrders.size());
        response.put("data", purchaseOrders);

        return ResponseEntity.ok(response);
    }

    /**
     * Send purchase order to supplier
     * PUT /api/procurement/purchase-orders/{poId}/send
     */
    @PutMapping("/purchase-orders/{poId}/send")
    public ResponseEntity<Map<String, Object>> sendPurchaseOrder(@PathVariable Long poId) {
        try {
            PurchaseOrderResponseDTO response = procurementService.sendPurchaseOrder(poId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Purchase order sent to supplier successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Receive goods for purchase order
     * PUT /api/procurement/purchase-orders/{poId}/receive
     */
    @PutMapping("/purchase-orders/{poId}/receive")
    public ResponseEntity<Map<String, Object>> receiveGoods(
            @PathVariable Long poId,
            @RequestParam Long itemId,
            @RequestParam Integer quantityReceived) {
        try {
            PurchaseOrderResponseDTO response = procurementService.receiveGoods(poId, itemId, quantityReceived);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Goods received successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Cancel purchase order
     * PUT /api/procurement/purchase-orders/{poId}/cancel
     */
    @PutMapping("/purchase-orders/{poId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelPurchaseOrder(@PathVariable Long poId) {
        try {
            PurchaseOrderResponseDTO response = procurementService.cancelPurchaseOrder(poId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Purchase order cancelled successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get overdue purchase orders
     * GET /api/procurement/purchase-orders/overdue
     */
    @GetMapping("/purchase-orders/overdue")
    public ResponseEntity<Map<String, Object>> getOverduePurchaseOrders() {
        List<PurchaseOrderResponseDTO> purchaseOrders = procurementService.getOverduePurchaseOrders();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Overdue purchase orders");
        response.put("count", purchaseOrders.size());
        response.put("data", purchaseOrders);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete purchase order
     * DELETE /api/procurement/purchase-orders/{poId}
     */
    @DeleteMapping("/purchase-orders/{poId}")
    public ResponseEntity<Map<String, Object>> deletePurchaseOrder(@PathVariable Long poId) {
        try {
            procurementService.deletePurchaseOrder(poId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Purchase order deleted successfully");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
