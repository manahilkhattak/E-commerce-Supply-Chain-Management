package com.ecommerce.supplychain.receiving.controller;

import com.ecommerce.supplychain.receiving.dto.GoodsReceiptDTO;
import com.ecommerce.supplychain.receiving.dto.GoodsReceiptResponseDTO;
import com.ecommerce.supplychain.receiving.dto.InspectionDTO;
import com.ecommerce.supplychain.receiving.service.ReceivingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Goods Receiving and Inspection Management APIs.
 */
@RestController
@RequestMapping("/api/receiving")
@CrossOrigin(origins = "*")
public class ReceivingController {

    @Autowired
    private ReceivingService receivingService;

    /**
     * API 1: Create Goods Receipt when items arrive
     * POST /api/receiving/goods-receipts
     */
    @PostMapping("/goods-receipts")
    public ResponseEntity<Map<String, Object>> createGoodsReceipt(@Valid @RequestBody GoodsReceiptDTO dto) {
        try {
            GoodsReceiptResponseDTO response = receivingService.createGoodsReceipt(dto);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Goods receipt created successfully");
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
     * API 2: Perform Quality Inspection on received goods
     * POST /api/receiving/inspections
     */
    @PostMapping("/inspections")
    public ResponseEntity<Map<String, Object>> performInspection(@Valid @RequestBody InspectionDTO dto) {
        try {
            GoodsReceiptResponseDTO response = receivingService.performInspection(dto);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Quality inspection completed successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all goods receipts
     * GET /api/receiving/goods-receipts
     */
    @GetMapping("/goods-receipts")
    public ResponseEntity<Map<String, Object>> getAllGoodsReceipts() {
        List<GoodsReceiptResponseDTO> receipts = receivingService.getAllGoodsReceipts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", receipts.size());
        response.put("data", receipts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get goods receipt by ID
     * GET /api/receiving/goods-receipts/{receiptId}
     */
    @GetMapping("/goods-receipts/{receiptId}")
    public ResponseEntity<Map<String, Object>> getGoodsReceiptById(@PathVariable Long receiptId) {
        try {
            GoodsReceiptResponseDTO receipt = receivingService.getGoodsReceiptById(receiptId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", receipt);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get receipts by Purchase Order ID
     * GET /api/receiving/goods-receipts/po/{poId}
     */
    @GetMapping("/goods-receipts/po/{poId}")
    public ResponseEntity<Map<String, Object>> getReceiptsByPoId(@PathVariable Long poId) {
        List<GoodsReceiptResponseDTO> receipts = receivingService.getReceiptsByPoId(poId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("poId", poId);
        response.put("count", receipts.size());
        response.put("data", receipts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get receipts by Supplier ID
     * GET /api/receiving/goods-receipts/supplier/{supplierId}
     */
    @GetMapping("/goods-receipts/supplier/{supplierId}")
    public ResponseEntity<Map<String, Object>> getReceiptsBySupplierId(@PathVariable Long supplierId) {
        List<GoodsReceiptResponseDTO> receipts = receivingService.getReceiptsBySupplierId(supplierId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("supplierId", supplierId);
        response.put("count", receipts.size());
        response.put("data", receipts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get receipts by status
     * GET /api/receiving/goods-receipts/status/{status}
     */
    @GetMapping("/goods-receipts/status/{status}")
    public ResponseEntity<Map<String, Object>> getReceiptsByStatus(@PathVariable String status) {
        List<GoodsReceiptResponseDTO> receipts = receivingService.getReceiptsByStatus(status.toUpperCase());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status.toUpperCase());
        response.put("count", receipts.size());
        response.put("data", receipts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get receipts with discrepancies
     * GET /api/receiving/goods-receipts/discrepancies
     */
    @GetMapping("/goods-receipts/discrepancies")
    public ResponseEntity<Map<String, Object>> getReceiptsWithDiscrepancies() {
        List<GoodsReceiptResponseDTO> receipts = receivingService.getReceiptsWithDiscrepancies();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Receipts with quantity discrepancies");
        response.put("count", receipts.size());
        response.put("data", receipts);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete goods receipt
     * DELETE /api/receiving/goods-receipts/{receiptId}
     */
    @DeleteMapping("/goods-receipts/{receiptId}")
    public ResponseEntity<Map<String, Object>> deleteGoodsReceipt(@PathVariable Long receiptId) {
        try {
            receivingService.deleteGoodsReceipt(receiptId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Goods receipt deleted successfully");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}