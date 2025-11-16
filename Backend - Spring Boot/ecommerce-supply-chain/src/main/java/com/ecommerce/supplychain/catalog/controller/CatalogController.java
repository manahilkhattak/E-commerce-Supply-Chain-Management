package com.ecommerce.supplychain.catalog.controller;

import com.ecommerce.supplychain.catalog.dto.ProductDTO;
import com.ecommerce.supplychain.catalog.dto.ProductResponseDTO;
import com.ecommerce.supplychain.catalog.dto.StockEntryDTO;
import com.ecommerce.supplychain.catalog.dto.StockUpdateResponseDTO;
import com.ecommerce.supplychain.catalog.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Catalog and Stock Management APIs.
 */
@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    /**
     * API 1: Add new product to catalog
     * POST /api/catalog/products
     */
    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> addProductToCatalog(@Valid @RequestBody ProductDTO productDTO) {
        try {
            ProductResponseDTO response = catalogService.addProductToCatalog(productDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Product added to catalog successfully");
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
     * API 2: Update product stock
     * POST /api/catalog/stock-update
     */
    @PostMapping("/stock-update")
    public ResponseEntity<Map<String, Object>> updateProductStock(@Valid @RequestBody StockEntryDTO stockDTO) {
        try {
            StockUpdateResponseDTO response = catalogService.updateProductStock(stockDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Product stock updated successfully");
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
     * Get all products
     * GET /api/catalog/products
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        List<ProductResponseDTO> products = catalogService.getAllProducts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", products.size());
        response.put("data", products);

        return ResponseEntity.ok(response);
    }

    /**
     * Get product by ID
     * GET /api/catalog/products/{productId}
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long productId) {
        try {
            ProductResponseDTO product = catalogService.getProductById(productId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", product);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get product by SKU
     * GET /api/catalog/products/sku/{productSku}
     */
    @GetMapping("/products/sku/{productSku}")
    public ResponseEntity<Map<String, Object>> getProductBySku(@PathVariable String productSku) {
        try {
            ProductResponseDTO product = catalogService.getProductBySku(productSku);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", product);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get products by category
     * GET /api/catalog/products/category/{category}
     */
    @GetMapping("/products/category/{category}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDTO> products = catalogService.getProductsByCategory(category);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("category", category);
        response.put("count", products.size());
        response.put("data", products);

        return ResponseEntity.ok(response);
    }

    /**
     * Get products by supplier
     * GET /api/catalog/products/supplier/{supplierId}
     */
    @GetMapping("/products/supplier/{supplierId}")
    public ResponseEntity<Map<String, Object>> getProductsBySupplierId(@PathVariable Long supplierId) {
        List<ProductResponseDTO> products = catalogService.getProductsBySupplierId(supplierId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("supplierId", supplierId);
        response.put("count", products.size());
        response.put("data", products);

        return ResponseEntity.ok(response);
    }

    /**
     * Get products by status
     * GET /api/catalog/products/status/{status}
     */
    @GetMapping("/products/status/{status}")
    public ResponseEntity<Map<String, Object>> getProductsByStatus(@PathVariable String status) {
        List<ProductResponseDTO> products = catalogService.getProductsByStatus(status.toUpperCase());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status.toUpperCase());
        response.put("count", products.size());
        response.put("data", products);

        return ResponseEntity.ok(response);
    }

    /**
     * Get products needing reorder
     * GET /api/catalog/products/reorder-needed
     */
    @GetMapping("/products/reorder-needed")
    public ResponseEntity<Map<String, Object>> getProductsNeedingReorder() {
        List<ProductResponseDTO> products = catalogService.getProductsNeedingReorder();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Products that need reordering");
        response.put("count", products.size());
        response.put("data", products);

        return ResponseEntity.ok(response);
    }

    /**
     * Get low stock products
     * GET /api/catalog/products/low-stock
     */
    @GetMapping("/products/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockProducts() {
        List<ProductResponseDTO> products = catalogService.getLowStockProducts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Products with low stock levels");
        response.put("count", products.size());
        response.put("data", products);

        return ResponseEntity.ok(response);
    }

    /**
     * Search products
     * GET /api/catalog/products/search?q={searchTerm}
     */
    @GetMapping("/products/search")
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam String q) {
        List<ProductResponseDTO> products = catalogService.searchProducts(q);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("searchTerm", q);
        response.put("count", products.size());
        response.put("data", products);

        return ResponseEntity.ok(response);
    }

    /**
     * Update product information
     * PUT /api/catalog/products/{productId}
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDTO productDTO) {
        try {
            ProductResponseDTO response = catalogService.updateProduct(productId, productDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Product updated successfully");
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
     * Delete product (soft delete)
     * DELETE /api/catalog/products/{productId}
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long productId) {
        try {
            catalogService.deleteProduct(productId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product deleted successfully");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}