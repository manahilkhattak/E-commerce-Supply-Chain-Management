package com.ecommerce.supplychain.procurement.service;

import com.ecommerce.supplychain.procurement.dto.PurchaseOrderDTO;
import com.ecommerce.supplychain.procurement.dto.PurchaseOrderResponseDTO;
import com.ecommerce.supplychain.procurement.model.PurchaseOrder;
import com.ecommerce.supplychain.procurement.model.PurchaseOrderItem;
import com.ecommerce.supplychain.procurement.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling procurement and purchase order business logic.
 */
@Service
public class ProcurementService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    /**
     * Create a new purchase order with items.
     */
    @Transactional
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderDTO dto) {
        // Validate PO number uniqueness
        if (purchaseOrderRepository.existsByPoNumber(dto.getPoNumber())) {
            throw new IllegalArgumentException("Purchase order with number " + dto.getPoNumber() + " already exists");
        }

        // Create PurchaseOrder entity
        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(dto.getPoNumber());
        po.setSupplierId(dto.getSupplierId());
        po.setContractId(dto.getContractId());
        po.setOrderDate(LocalDate.now());
        po.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        po.setTaxAmount(dto.getTaxAmount() != null ? dto.getTaxAmount() : BigDecimal.ZERO);
        po.setDiscountAmount(dto.getDiscountAmount() != null ? dto.getDiscountAmount() : BigDecimal.ZERO);
        po.setCurrency(dto.getCurrency());
        po.setPaymentTerms(dto.getPaymentTerms());
        po.setDeliveryAddress(dto.getDeliveryAddress());
        po.setRequestedBy(dto.getRequestedBy());
        po.setNotes(dto.getNotes());
        po.setStatus("DRAFT");
        po.setCreatedAt(LocalDateTime.now());
        po.setUpdatedAt(LocalDateTime.now());

        // Add items to PO
        for (PurchaseOrderDTO.PurchaseOrderItemDTO itemDTO : dto.getItems()) {
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setProductName(itemDTO.getProductName());
            item.setProductSku(itemDTO.getProductSku());
            item.setQuantity(itemDTO.getQuantity());
            item.setReceivedQuantity(0);
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setTaxRate(itemDTO.getTaxRate() != null ? itemDTO.getTaxRate() : BigDecimal.ZERO);
            item.setDiscountRate(itemDTO.getDiscountRate() != null ? itemDTO.getDiscountRate() : BigDecimal.ZERO);
            item.setUnitOfMeasurement(itemDTO.getUnitOfMeasurement());
            item.setNotes(itemDTO.getNotes());
            item.calculateLineTotal();

            po.addItem(item);
        }

        // Calculate total amount
        po.calculateTotalAmount();

        // Save PO with items
        PurchaseOrder savedPO = purchaseOrderRepository.save(po);

        return mapToResponseDTO(savedPO);
    }

    /**
     * Approve a purchase order.
     */
    @Transactional
    public PurchaseOrderResponseDTO approvePurchaseOrder(Long poId, String approvedBy) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found with ID: " + poId));

        if (!"DRAFT".equals(po.getStatus()) && !"PENDING_APPROVAL".equals(po.getStatus())) {
            throw new IllegalStateException("Only DRAFT or PENDING_APPROVAL purchase orders can be approved");
        }

        po.setStatus("APPROVED");
        po.setApprovedBy(approvedBy);
        po.setApprovalDate(LocalDate.now());
        po.setUpdatedAt(LocalDateTime.now());

        PurchaseOrder updatedPO = purchaseOrderRepository.save(po);

        return mapToResponseDTO(updatedPO);
    }

    /**
     * Get all purchase orders.
     */
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get purchase order by ID.
     */
    public PurchaseOrderResponseDTO getPurchaseOrderById(Long poId) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found with ID: " + poId));
        return mapToResponseDTO(po);
    }

    /**
     * Get purchase orders by supplier.
     */
    public List<PurchaseOrderResponseDTO> getPurchaseOrdersBySupplierId(Long supplierId) {
        return purchaseOrderRepository.findBySupplierId(supplierId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get purchase orders by status.
     */
    public List<PurchaseOrderResponseDTO> getPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Send purchase order to supplier (change status to SENT).
     */
    @Transactional
    public PurchaseOrderResponseDTO sendPurchaseOrder(Long poId) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found with ID: " + poId));

        if (!"APPROVED".equals(po.getStatus())) {
            throw new IllegalStateException("Only APPROVED purchase orders can be sent");
        }

        po.setStatus("SENT");
        po.setUpdatedAt(LocalDateTime.now());

        PurchaseOrder updatedPO = purchaseOrderRepository.save(po);

        return mapToResponseDTO(updatedPO);
    }

    /**
     * Receive goods for a purchase order (update quantities).
     */
    @Transactional
    public PurchaseOrderResponseDTO receiveGoods(Long poId, Long itemId, Integer quantityReceived) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found with ID: " + poId));

        if (!"SENT".equals(po.getStatus()) && !"PARTIALLY_RECEIVED".equals(po.getStatus())) {
            throw new IllegalStateException("Can only receive goods for SENT or PARTIALLY_RECEIVED orders");
        }

        // Find the item
        PurchaseOrderItem item = po.getItems().stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in purchase order"));

        // Update received quantity
        int newReceivedQty = item.getReceivedQuantity() + quantityReceived;
        if (newReceivedQty > item.getQuantity()) {
            throw new IllegalArgumentException("Received quantity cannot exceed ordered quantity");
        }

        item.setReceivedQuantity(newReceivedQty);
        item.setUpdatedAt(LocalDateTime.now());

        // Check if all items are fully received
        boolean allItemsReceived = po.getItems().stream()
                .allMatch(i -> i.getReceivedQuantity().equals(i.getQuantity()));

        if (allItemsReceived) {
            po.setStatus("RECEIVED");
            po.setActualDeliveryDate(LocalDate.now());
        } else {
            po.setStatus("PARTIALLY_RECEIVED");
        }

        po.setUpdatedAt(LocalDateTime.now());

        PurchaseOrder updatedPO = purchaseOrderRepository.save(po);

        return mapToResponseDTO(updatedPO);
    }

    /**
     * Cancel a purchase order.
     */
    @Transactional
    public PurchaseOrderResponseDTO cancelPurchaseOrder(Long poId) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found with ID: " + poId));

        if ("RECEIVED".equals(po.getStatus()) || "CLOSED".equals(po.getStatus())) {
            throw new IllegalStateException("Cannot cancel RECEIVED or CLOSED purchase orders");
        }

        po.setStatus("CANCELLED");
        po.setUpdatedAt(LocalDateTime.now());

        PurchaseOrder updatedPO = purchaseOrderRepository.save(po);

        return mapToResponseDTO(updatedPO);
    }

    /**
     * Get overdue purchase orders.
     */
    public List<PurchaseOrderResponseDTO> getOverduePurchaseOrders() {
        return purchaseOrderRepository.findOverduePurchaseOrders(LocalDate.now()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete purchase order (soft delete).
     */
    @Transactional
    public void deletePurchaseOrder(Long poId) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found with ID: " + poId));

        if (!"DRAFT".equals(po.getStatus()) && !"CANCELLED".equals(po.getStatus())) {
            throw new IllegalStateException("Only DRAFT or CANCELLED purchase orders can be deleted");
        }

        purchaseOrderRepository.delete(po);
    }

    /**
     * Helper method to convert PurchaseOrder to ResponseDTO.
     */
    private PurchaseOrderResponseDTO mapToResponseDTO(PurchaseOrder po) {
        Integer daysUntilDelivery = null;
        if (po.getExpectedDeliveryDate() != null && po.getActualDeliveryDate() == null) {
            daysUntilDelivery = (int) ChronoUnit.DAYS.between(LocalDate.now(), po.getExpectedDeliveryDate());
        }

        List<PurchaseOrderResponseDTO.PurchaseOrderItemResponseDTO> itemDTOs = po.getItems().stream()
                .map(item -> PurchaseOrderResponseDTO.PurchaseOrderItemResponseDTO.builder()
                        .itemId(item.getItemId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productSku(item.getProductSku())
                        .quantity(item.getQuantity())
                        .receivedQuantity(item.getReceivedQuantity())
                        .unitPrice(item.getUnitPrice())
                        .lineTotal(item.getLineTotal())
                        .taxRate(item.getTaxRate())
                        .discountRate(item.getDiscountRate())
                        .unitOfMeasurement(item.getUnitOfMeasurement())
                        .notes(item.getNotes())
                        .fullyReceived(item.getReceivedQuantity().equals(item.getQuantity()))
                        .build())
                .collect(Collectors.toList());

        return PurchaseOrderResponseDTO.builder()
                .poId(po.getPoId())
                .poNumber(po.getPoNumber())
                .supplierId(po.getSupplierId())
                .contractId(po.getContractId())
                .orderDate(po.getOrderDate())
                .expectedDeliveryDate(po.getExpectedDeliveryDate())
                .actualDeliveryDate(po.getActualDeliveryDate())
                .status(po.getStatus())
                .totalAmount(po.getTotalAmount())
                .taxAmount(po.getTaxAmount())
                .discountAmount(po.getDiscountAmount())
                .finalAmount(po.getFinalAmount())
                .currency(po.getCurrency())
                .paymentTerms(po.getPaymentTerms())
                .deliveryAddress(po.getDeliveryAddress())
                .requestedBy(po.getRequestedBy())
                .approvedBy(po.getApprovedBy())
                .approvalDate(po.getApprovalDate())
                .notes(po.getNotes())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .totalItems(po.getItems().size())
                .items(itemDTOs)
                .daysUntilDelivery(daysUntilDelivery)
                .build();
    }
}