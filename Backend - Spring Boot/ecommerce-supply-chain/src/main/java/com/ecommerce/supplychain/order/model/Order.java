package com.ecommerce.supplychain.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "customer_name", nullable = false, length = 255)
    private String customerName;

    @Column(name = "customer_email", nullable = false, length = 100)
    private String customerEmail;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "shipping_address", nullable = false, columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "billing_address", columnDefinition = "TEXT")
    private String billingAddress;

    @Column(name = "order_status", nullable = false, length = 50)
    private String orderStatus; // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED

    @Column(name = "payment_status", length = 50)
    private String paymentStatus; // PENDING, PAID, FAILED, REFUNDED

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // CREDIT_CARD, PAYPAL, BANK_TRANSFER

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "shipping_cost", precision = 8, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "tax_amount", precision = 8, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "discount_amount", precision = 8, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "final_amount", precision = 12, scale = 2)
    private BigDecimal finalAmount;

    @Column(name = "currency", length = 3)
    private String currency; // USD, EUR, GBP

    @Column(name = "warehouse_id")
    private Long warehouseId; // Links to Process 8 (Warehouse)

    @Column(name = "priority_level", length = 20)
    private String priorityLevel; // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "estimated_delivery_date")
    private LocalDateTime estimatedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Column(name = "order_notes", columnDefinition = "TEXT")
    private String orderNotes;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Links to other processes
    @Column(name = "pick_list_id")
    private Long pickListId; // Links to Process 9 (Picking)

    @Column(name = "shipment_id")
    private Long shipmentId; // Links to Process 11 (Shipment)

    @Column(name = "quality_check_id")
    private Long qualityCheckId; // Links to Process 10 (Quality)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (orderStatus == null) {
            orderStatus = "PENDING";
        }
        if (paymentStatus == null) {
            paymentStatus = "PENDING";
        }
        if (currency == null) {
            currency = "USD";
        }
        if (priorityLevel == null) {
            priorityLevel = "MEDIUM";
        }
        if (shippingCost == null) {
            shippingCost = BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Calculate total order amount
     */
    public void calculateTotals() {
        if (orderItems != null && !orderItems.isEmpty()) {
            // Calculate subtotal from items
            BigDecimal subtotal = orderItems.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            this.totalAmount = subtotal;
            this.finalAmount = subtotal
                    .add(shippingCost != null ? shippingCost : BigDecimal.ZERO)
                    .add(taxAmount != null ? taxAmount : BigDecimal.ZERO)
                    .subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        }
    }

    /**
     * Check if order can be cancelled
     */
    public boolean canBeCancelled() {
        return !List.of("SHIPPED", "DELIVERED", "CANCELLED", "REFUNDED").contains(orderStatus);
    }

    /**
     * Check if order requires quality check
     */
    public boolean requiresQualityCheck() {
        return orderItems.stream().anyMatch(item ->
                item.getRequiresQualityCheck() != null && item.getRequiresQualityCheck()
        );
    }
}