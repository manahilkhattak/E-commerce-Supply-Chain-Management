package com.ecommerce.supplychain.catalog.service;

import com.ecommerce.supplychain.catalog.dto.ProductDTO;
import com.ecommerce.supplychain.catalog.dto.ProductResponseDTO;
import com.ecommerce.supplychain.catalog.dto.StockEntryDTO;
import com.ecommerce.supplychain.catalog.dto.StockUpdateResponseDTO;
import com.ecommerce.supplychain.catalog.model.Product;
import com.ecommerce.supplychain.catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling catalog and stock management business logic.
 */
@Service
public class CatalogService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * API 1: Add new product to catalog
     */
    @Transactional
    public ProductResponseDTO addProductToCatalog(ProductDTO productDTO) {
        // Validate SKU uniqueness
        if (productRepository.existsByProductSku(productDTO.getProductSku())) {
            throw new IllegalArgumentException("Product with SKU " + productDTO.getProductSku() + " already exists");
        }

        // Create product entity
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setProductSku(productDTO.getProductSku());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setBrand(productDTO.getBrand());
        product.setSupplierId(productDTO.getSupplierId());
        product.setCostPrice(productDTO.getCostPrice());
        product.setSellingPrice(productDTO.getSellingPrice());
        product.setCurrentStock(productDTO.getCurrentStock());
        product.setMinimumStockLevel(productDTO.getMinimumStockLevel());
        product.setMaximumStockLevel(productDTO.getMaximumStockLevel());
        product.setReorderPoint(productDTO.getReorderPoint());
        product.setWeight(productDTO.getWeight());
        product.setDimensions(productDTO.getDimensions());
        product.setUnitOfMeasurement(productDTO.getUnitOfMeasurement());
        product.setBarcode(productDTO.getBarcode());
        product.setImageUrl(productDTO.getImageUrl());
        product.setIsActive(productDTO.getIsActive() != null ? productDTO.getIsActive() : true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setLastStockUpdate(LocalDateTime.now());

        // Auto-set status based on stock
        if (product.getCurrentStock() <= 0) {
            product.setStatus("OUT_OF_STOCK");
        } else if (product.getCurrentStock() <= product.getReorderPoint()) {
            product.setStatus("LOW_STOCK");
        } else {
            product.setStatus("ACTIVE");
        }

        Product savedProduct = productRepository.save(product);

        return mapToProductResponseDTO(savedProduct);
    }

    /**
     * API 2: Update product stock
     */
    @Transactional
    public StockUpdateResponseDTO updateProductStock(StockEntryDTO stockDTO) {
        Product product = productRepository.findById(stockDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + stockDTO.getProductId()));

        int previousStock = product.getCurrentStock();
        int newStock;

        switch (stockDTO.getAdjustmentType().toUpperCase()) {
            case "ADD":
                newStock = previousStock + stockDTO.getQuantity();
                break;
            case "REMOVE":
                newStock = previousStock - stockDTO.getQuantity();
                if (newStock < 0) {
                    throw new IllegalArgumentException("Cannot remove more stock than available. Available: " + previousStock);
                }
                break;
            case "SET":
                newStock = stockDTO.getQuantity();
                if (newStock < 0) {
                    throw new IllegalArgumentException("Stock cannot be set to negative value");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid adjustment type: " + stockDTO.getAdjustmentType());
        }

        // Check against maximum stock level
        if (product.getMaximumStockLevel() != null && newStock > product.getMaximumStockLevel()) {
            throw new IllegalArgumentException("Stock cannot exceed maximum level of " + product.getMaximumStockLevel());
        }

        product.setCurrentStock(newStock);
        product.setLastStockUpdate(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);

        return mapToStockUpdateResponseDTO(updatedProduct, previousStock, stockDTO);
    }

    /**
     * Get all products
     */
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get product by ID
     */
    public ProductResponseDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        return mapToProductResponseDTO(product);
    }

    /**
     * Get product by SKU
     */
    public ProductResponseDTO getProductBySku(String productSku) {
        Product product = productRepository.findByProductSku(productSku)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with SKU: " + productSku));
        return mapToProductResponseDTO(product);
    }

    /**
     * Get products by category
     */
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get products by supplier
     */
    public List<ProductResponseDTO> getProductsBySupplierId(Long supplierId) {
        return productRepository.findBySupplierId(supplierId).stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get products by status
     */
    public List<ProductResponseDTO> getProductsByStatus(String status) {
        return productRepository.findByStatus(status).stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get products needing reorder
     */
    public List<ProductResponseDTO> getProductsNeedingReorder() {
        return productRepository.findProductsNeedingReorder().stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get low stock products
     */
    public List<ProductResponseDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search products by name or SKU
     */
    public List<ProductResponseDTO> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm).stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update product information
     */
    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        // Check SKU uniqueness if changed
        if (!product.getProductSku().equals(productDTO.getProductSku()) &&
                productRepository.existsByProductSku(productDTO.getProductSku())) {
            throw new IllegalArgumentException("Product with SKU " + productDTO.getProductSku() + " already exists");
        }

        product.setProductName(productDTO.getProductName());
        product.setProductSku(productDTO.getProductSku());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setBrand(productDTO.getBrand());
        product.setSupplierId(productDTO.getSupplierId());
        product.setCostPrice(productDTO.getCostPrice());
        product.setSellingPrice(productDTO.getSellingPrice());
        product.setMinimumStockLevel(productDTO.getMinimumStockLevel());
        product.setMaximumStockLevel(productDTO.getMaximumStockLevel());
        product.setReorderPoint(productDTO.getReorderPoint());
        product.setWeight(productDTO.getWeight());
        product.setDimensions(productDTO.getDimensions());
        product.setUnitOfMeasurement(productDTO.getUnitOfMeasurement());
        product.setBarcode(productDTO.getBarcode());
        product.setImageUrl(productDTO.getImageUrl());
        product.setIsActive(productDTO.getIsActive());
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);

        return mapToProductResponseDTO(updatedProduct);
    }

    /**
     * Delete product (soft delete by setting inactive)
     */
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        product.setIsActive(false);
        product.setStatus("INACTIVE");
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    /**
     * Helper method to convert Product to ProductResponseDTO
     */
    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        boolean needsReorder = product.getCurrentStock() <= product.getReorderPoint();
        int stockDeficit = Math.max(0, product.getMinimumStockLevel() - product.getCurrentStock());

        return ProductResponseDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productSku(product.getProductSku())
                .description(product.getDescription())
                .category(product.getCategory())
                .brand(product.getBrand())
                .supplierId(product.getSupplierId())
                .costPrice(product.getCostPrice())
                .sellingPrice(product.getSellingPrice())
                .currentStock(product.getCurrentStock())
                .minimumStockLevel(product.getMinimumStockLevel())
                .maximumStockLevel(product.getMaximumStockLevel())
                .reorderPoint(product.getReorderPoint())
                .weight(product.getWeight())
                .dimensions(product.getDimensions())
                .unitOfMeasurement(product.getUnitOfMeasurement())
                .barcode(product.getBarcode())
                .imageUrl(product.getImageUrl())
                .isActive(product.getIsActive())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .lastStockUpdate(product.getLastStockUpdate())
                .needsReorder(needsReorder)
                .stockDeficit(stockDeficit)
                .build();
    }

    /**
     * Helper method to convert to StockUpdateResponseDTO
     */
    private StockUpdateResponseDTO mapToStockUpdateResponseDTO(Product product, int previousStock, StockEntryDTO stockDTO) {
        String stockLevel;
        if (product.getCurrentStock() == 0) {
            stockLevel = "OUT_OF_STOCK";
        } else if (product.getCurrentStock() <= product.getReorderPoint()) {
            stockLevel = "LOW_STOCK";
        } else if (product.getCurrentStock() <= product.getMinimumStockLevel()) {
            stockLevel = "CRITICAL";
        } else {
            stockLevel = "NORMAL";
        }

        boolean stockAlert = product.getCurrentStock() <= product.getReorderPoint();

        return StockUpdateResponseDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productSku(product.getProductSku())
                .previousStock(previousStock)
                .newStock(product.getCurrentStock())
                .quantityChanged(Math.abs(product.getCurrentStock() - previousStock))
                .adjustmentType(stockDTO.getAdjustmentType())
                .reason(stockDTO.getReason())
                .referenceNumber(stockDTO.getReferenceNumber())
                .updatedBy(stockDTO.getUpdatedBy())
                .updatedAt(product.getLastStockUpdate())
                .status(product.getStatus())
                .stockAlert(stockAlert)
                .stockLevel(stockLevel)
                .build();
    }
}