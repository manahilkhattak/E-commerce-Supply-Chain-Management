package com.ecommerce.supplychain.picking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; /**
 * Entity representing individual items in a pick list.
 * Links to specific shelf locations in the warehouse.
 */
@Entity
@Table(name = "pick_list_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_item_id")
    private Long pickItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_list_id", nullable = false)
    private PickList pickList;

    @Column(name = "product_id", nullable = false)
    private Long productId; // Reference to Product from Process 5

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "shelf_location_id", nullable = false)
    private Long shelfLocationId; // Reference to ShelfLocation from Process 8

    @Column(name = "location_code", nullable = false, length = 50)
    private String locationCode;

    @Column(name = "required_quantity", nullable = false)
    private Integer requiredQuantity;

    @Column(name = "picked_quantity")
    private Integer pickedQuantity;

    @Column(name = "is_picked")
    private Boolean isPicked;

    @Column(name = "pick_sequence")
    private Integer pickSequence;

    @Column(name = "zone_code", length = 20)
    private String zoneCode;

    @Column(name = "aisle_number", length = 10)
    private String aisleNumber;

    @Column(name = "weight_per_unit_kg")
    private Double weightPerUnitKg;

    @Column(name = "pick_notes", columnDefinition = "TEXT")
    private String pickNotes;

    @Column(name = "picked_at")
    private LocalDateTime pickedAt;

    @PrePersist
    protected void onCreate() {
        if (pickedQuantity == null) {
            pickedQuantity = 0;
        }
        if (isPicked == null) {
            isPicked = false;
        }
        if (pickSequence == null) {
            pickSequence = 1;
        }
    }
}
