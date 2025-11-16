package com.ecommerce.supplychain.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends RuntimeException {

    private Long productId;
    private String productName;
    private Integer requestedQuantity;
    private Integer availableQuantity;

    public InsufficientStockException(Long productId, String productName,
                                      Integer requestedQuantity, Integer availableQuantity) {
        super(String.format(
                "Insufficient stock for product '%s' (ID: %d). Requested: %d, Available: %d",
                productName, productId, requestedQuantity, availableQuantity
        ));
        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public InsufficientStockException(String message) {
        super(message);
    }

    // Getters
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getRequestedQuantity() { return requestedQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
}