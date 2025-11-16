package com.ecommerce.supplychain.receiving.service;

import com.ecommerce.supplychain.receiving.dto.GoodsReceiptDTO;
import com.ecommerce.supplychain.receiving.dto.GoodsReceiptResponseDTO;
import com.ecommerce.supplychain.receiving.dto.InspectionDTO;
import com.ecommerce.supplychain.receiving.model.GoodsReceipt;
import com.ecommerce.supplychain.receiving.model.InspectionRecord;
import com.ecommerce.supplychain.receiving.repository.GoodsReceiptRepository;
import com.ecommerce.supplychain.receiving.repository.InspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Goods Receiving and Inspection operations.
 */
@Service
public class ReceivingService {

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private InspectionRepository inspectionRepository;

    /**
     * API 1: Create goods receipt when items arrive from supplier
     */
    @Transactional
    public GoodsReceiptResponseDTO createGoodsReceipt(GoodsReceiptDTO dto) {
        // Validate receipt number uniqueness
        if (goodsReceiptRepository.existsByReceiptNumber(dto.getReceiptNumber())) {
            throw new IllegalArgumentException("Receipt with number " + dto.getReceiptNumber() + " already exists");
        }

        // Create GoodsReceipt entity
        GoodsReceipt receipt = new GoodsReceipt();
        receipt.setReceiptNumber(dto.getReceiptNumber());
        receipt.setPoId(dto.getPoId());
        receipt.setPoNumber(dto.getPoNumber());
        receipt.setSupplierId(dto.getSupplierId());
        receipt.setReceiptDate(dto.getReceiptDate() != null ? dto.getReceiptDate() : LocalDate.now());
        receipt.setReceivedBy(dto.getReceivedBy());
        receipt.setWarehouseLocation(dto.getWarehouseLocation());
        receipt.setDeliveryNoteNumber(dto.getDeliveryNoteNumber());
        receipt.setVehicleNumber(dto.getVehicleNumber());
        receipt.setNotes(dto.getNotes());
        receipt.setStatus("RECEIVED");

        // Calculate totals
        int totalOrdered = 0;
        int totalReceived = 0;
        boolean discrepancy = false;

        // Create inspection records for each item
        for (GoodsReceiptDTO.InspectionItemDTO itemDTO : dto.getItems()) {
            InspectionRecord record = new InspectionRecord();
            record.setProductId(itemDTO.getProductId());
            record.setProductName(itemDTO.getProductName());
            record.setProductSku(itemDTO.getProductSku());
            record.setOrderedQuantity(itemDTO.getOrderedQuantity());
            record.setReceivedQuantity(itemDTO.getReceivedQuantity());
            record.setBatchNumber(itemDTO.getBatchNumber());
            record.setExpiryDate(itemDTO.getExpiryDate());
            record.setInspectionNotes(itemDTO.getNotes());
            record.setInspectionStatus("PENDING");
            record.setInspectorName("N/A");
            record.setInspectionDate(LocalDateTime.now());

            totalOrdered += itemDTO.getOrderedQuantity();
            totalReceived += itemDTO.getReceivedQuantity();

            if (!itemDTO.getOrderedQuantity().equals(itemDTO.getReceivedQuantity())) {
                discrepancy = true;
            }

            receipt.addInspectionRecord(record);
        }

        receipt.setTotalItemsOrdered(totalOrdered);
        receipt.setTotalItemsReceived(totalReceived);
        receipt.setDiscrepancyFound(discrepancy);

        GoodsReceipt savedReceipt = goodsReceiptRepository.save(receipt);

        return mapToResponseDTO(savedReceipt);
    }

    /**
     * API 2: Perform quality inspection on received goods
     */
    @Transactional
    public GoodsReceiptResponseDTO performInspection(InspectionDTO dto) {
        // Fetch the goods receipt
        GoodsReceipt receipt = goodsReceiptRepository.findById(dto.getReceiptId())
                .orElseThrow(() -> new IllegalArgumentException("Goods receipt not found with ID: " + dto.getReceiptId()));

        // Fetch the inspection record
        InspectionRecord record = inspectionRepository.findById(dto.getInspectionId())
                .orElseThrow(() -> new IllegalArgumentException("Inspection record not found with ID: " + dto.getInspectionId()));

        // Validate quantities
        int totalInspected = dto.getAcceptedQuantity() +
                (dto.getRejectedQuantity() != null ? dto.getRejectedQuantity() : 0) +
                (dto.getDamagedQuantity() != null ? dto.getDamagedQuantity() : 0);

        if (totalInspected > record.getReceivedQuantity()) {
            throw new IllegalArgumentException("Total inspected quantity cannot exceed received quantity");
        }

        // Update inspection record
        record.setAcceptedQuantity(dto.getAcceptedQuantity());
        record.setRejectedQuantity(dto.getRejectedQuantity() != null ? dto.getRejectedQuantity() : 0);
        record.setDamagedQuantity(dto.getDamagedQuantity() != null ? dto.getDamagedQuantity() : 0);
        record.setInspectorName(dto.getInspectorName());
        record.setQualityRating(dto.getQualityRating());
        record.setDefectType(dto.getDefectType());
        record.setActionTaken(dto.getActionTaken());
        record.setInspectionNotes(dto.getInspectionNotes());
        record.setPhotoUrl(dto.getPhotoUrl());
        record.setInspectionDate(LocalDateTime.now());

        // Determine inspection status
        if ("ACCEPT_ALL".equals(dto.getActionTaken()) && dto.getRejectedQuantity() == 0) {
            record.setInspectionStatus("PASSED");
        } else if ("REJECT_ALL".equals(dto.getActionTaken())) {
            record.setInspectionStatus("FAILED");
        } else {
            record.setInspectionStatus("CONDITIONAL");
        }

        inspectionRepository.save(record);

        // Check if all items have been inspected
        boolean allInspected = receipt.getInspectionRecords().stream()
                .allMatch(r -> !"PENDING".equals(r.getInspectionStatus()));

        if (allInspected) {
            // Check if all passed
            boolean allPassed = receipt.getInspectionRecords().stream()
                    .allMatch(r -> "PASSED".equals(r.getInspectionStatus()));

            // Check if any failed
            boolean anyFailed = receipt.getInspectionRecords().stream()
                    .anyMatch(r -> "FAILED".equals(r.getInspectionStatus()));

            if (allPassed) {
                receipt.setStatus("ACCEPTED");
            } else if (anyFailed) {
                receipt.setStatus("REJECTED");
            } else {
                receipt.setStatus("PARTIALLY_ACCEPTED");
            }

            receipt.setUpdatedAt(LocalDateTime.now());
            goodsReceiptRepository.save(receipt);
        } else {
            receipt.setStatus("INSPECTED");
            receipt.setUpdatedAt(LocalDateTime.now());
            goodsReceiptRepository.save(receipt);
        }

        // Reload to get updated data
        GoodsReceipt updatedReceipt = goodsReceiptRepository.findById(dto.getReceiptId())
                .orElseThrow(() -> new IllegalArgumentException("Receipt not found"));

        return mapToResponseDTO(updatedReceipt);
    }

    /**
     * Get all goods receipts
     */
    public List<GoodsReceiptResponseDTO> getAllGoodsReceipts() {
        return goodsReceiptRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get goods receipt by ID
     */
    public GoodsReceiptResponseDTO getGoodsReceiptById(Long receiptId) {
        GoodsReceipt receipt = goodsReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new IllegalArgumentException("Goods receipt not found with ID: " + receiptId));
        return mapToResponseDTO(receipt);
    }

    /**
     * Get receipts by PO ID
     */
    public List<GoodsReceiptResponseDTO> getReceiptsByPoId(Long poId) {
        return goodsReceiptRepository.findByPoId(poId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get receipts by supplier
     */
    public List<GoodsReceiptResponseDTO> getReceiptsBySupplierId(Long supplierId) {
        return goodsReceiptRepository.findBySupplierId(supplierId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get receipts by status
     */
    public List<GoodsReceiptResponseDTO> getReceiptsByStatus(String status) {
        return goodsReceiptRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get receipts with discrepancies
     */
    public List<GoodsReceiptResponseDTO> getReceiptsWithDiscrepancies() {
        return goodsReceiptRepository.findReceiptsWithDiscrepancies().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete goods receipt
     */
    @Transactional
    public void deleteGoodsReceipt(Long receiptId) {
        GoodsReceipt receipt = goodsReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new IllegalArgumentException("Goods receipt not found with ID: " + receiptId));

        if (!"PENDING".equals(receipt.getStatus()) && !"REJECTED".equals(receipt.getStatus())) {
            throw new IllegalStateException("Only PENDING or REJECTED receipts can be deleted");
        }

        goodsReceiptRepository.delete(receipt);
    }

    /**
     * Helper method to map GoodsReceipt to ResponseDTO
     */
    private GoodsReceiptResponseDTO mapToResponseDTO(GoodsReceipt receipt) {
        List<GoodsReceiptResponseDTO.InspectionRecordResponseDTO> recordDTOs =
                receipt.getInspectionRecords().stream()
                        .map(record -> {
                            Integer discrepancy = record.getOrderedQuantity() - record.getReceivedQuantity();
                            Boolean quantityMatch = record.getOrderedQuantity().equals(record.getReceivedQuantity());

                            return GoodsReceiptResponseDTO.InspectionRecordResponseDTO.builder()
                                    .inspectionId(record.getInspectionId())
                                    .productId(record.getProductId())
                                    .productName(record.getProductName())
                                    .productSku(record.getProductSku())
                                    .orderedQuantity(record.getOrderedQuantity())
                                    .receivedQuantity(record.getReceivedQuantity())
                                    .acceptedQuantity(record.getAcceptedQuantity())
                                    .rejectedQuantity(record.getRejectedQuantity())
                                    .damagedQuantity(record.getDamagedQuantity())
                                    .inspectionStatus(record.getInspectionStatus())
                                    .qualityRating(record.getQualityRating())
                                    .defectType(record.getDefectType())
                                    .batchNumber(record.getBatchNumber())
                                    .expiryDate(record.getExpiryDate())
                                    .inspectorName(record.getInspectorName())
                                    .inspectionDate(record.getInspectionDate())
                                    .inspectionNotes(record.getInspectionNotes())
                                    .actionTaken(record.getActionTaken())
                                    .quantityMatch(quantityMatch)
                                    .discrepancy(discrepancy)
                                    .build();
                        })
                        .collect(Collectors.toList());

        return GoodsReceiptResponseDTO.builder()
                .receiptId(receipt.getReceiptId())
                .receiptNumber(receipt.getReceiptNumber())
                .poId(receipt.getPoId())
                .poNumber(receipt.getPoNumber())
                .supplierId(receipt.getSupplierId())
                .receiptDate(receipt.getReceiptDate())
                .receivedBy(receipt.getReceivedBy())
                .warehouseLocation(receipt.getWarehouseLocation())
                .deliveryNoteNumber(receipt.getDeliveryNoteNumber())
                .vehicleNumber(receipt.getVehicleNumber())
                .status(receipt.getStatus())
                .totalItemsOrdered(receipt.getTotalItemsOrdered())
                .totalItemsReceived(receipt.getTotalItemsReceived())
                .discrepancyFound(receipt.getDiscrepancyFound())
                .notes(receipt.getNotes())
                .createdAt(receipt.getCreatedAt())
                .updatedAt(receipt.getUpdatedAt())
                .totalInspectionRecords(receipt.getInspectionRecords().size())
                .inspectionRecords(recordDTOs)
                .build();
    }
}