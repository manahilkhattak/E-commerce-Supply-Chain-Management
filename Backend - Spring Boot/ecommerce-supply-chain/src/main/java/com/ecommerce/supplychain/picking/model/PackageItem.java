package com.ecommerce.supplychain.picking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal; /**
 * Entity representing items within a package.
 */
@Entity
@Table(name = "package_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_item_id")
    private Long packageItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package packageEntity;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "weight_kg", precision = 8, scale = 2, nullable = false)
    private BigDecimal weightKg;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "is_fragile")
    private Boolean isFragile;

    @Column(name = "requires_special_handling")
    private Boolean requiresSpecialHandling;

    @Column(name = "item_notes", columnDefinition = "TEXT")
    private String itemNotes;

    @PrePersist
    protected void onCreate() {
        if (isFragile == null) {
            isFragile = false;
        }
        if (requiresSpecialHandling == null) {
            requiresSpecialHandling = false;
        }
    }
}
