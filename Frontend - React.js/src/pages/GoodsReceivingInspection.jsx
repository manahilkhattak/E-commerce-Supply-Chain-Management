// src/pages/GoodsReceivingInspection.jsx
import { useState } from "react";
import {
  createGoodsReceipt,
  performInspection, // uses /receiving/inspections
} from "../services/api";

export default function GoodsReceivingInspection() {
  // -------- Goods Receipt (GoodsReceiptDTO) --------
  const [receiptNumber, setReceiptNumber] = useState("GRN-2025-001");
  const [poId, setPoId] = useState("1");           // numeric Long
  const [supplierId, setSupplierId] = useState("1"); // numeric Long
  const [receiptDate, setReceiptDate] = useState("2030-01-05");
  const [receivedBy, setReceivedBy] = useState("Warehouse Clerk");
  const [warehouseLocation, setWarehouseLocation] = useState("Main Warehouse A1");
  const [deliveryNoteNumber, setDeliveryNoteNumber] = useState("DN-0001");
  const [vehicleNumber, setVehicleNumber] = useState("ABC-123");
  const [grnNotes, setGrnNotes] = useState("");

  // Single line item (InspectionItemDTO)
  const [productId, setProductId] = useState("1"); // numeric Long
  const [productName, setProductName] = useState("Sample Product");
  const [productSku, setProductSku] = useState("SKU-001");
  const [orderedQuantity, setOrderedQuantity] = useState("100");
  const [receivedQuantity, setReceivedQuantity] = useState("95");
  const [batchNumber, setBatchNumber] = useState("BATCH-001");
  const [expiryDate, setExpiryDate] = useState("2031-01-01");
  const [itemNotes, setItemNotes] = useState("");

  // -------- Inspection (InspectionDTO) --------
  const [receiptIdForInspection, setReceiptIdForInspection] = useState(""); // numeric Long (returned from GRN)
  const [inspectionId, setInspectionId] = useState("1"); // numeric Long
  const [acceptedQuantity, setAcceptedQuantity] = useState("95");
  const [rejectedQuantity, setRejectedQuantity] = useState("5");
  const [damagedQuantity, setDamagedQuantity] = useState("0");
  const [inspectorName, setInspectorName] = useState("Quality Inspector");
  const [qualityRating, setQualityRating] = useState("5"); // 1–5
  const [defectType, setDefectType] = useState("NONE");
  const [actionTaken, setActionTaken] = useState("ACCEPT_ALL");
  const [inspectionNotes, setInspectionNotes] = useState("");
  const [photoUrl, setPhotoUrl] = useState("");

  const [result, setResult] = useState(null);

  // -------------------------------------------
  // Create Goods Receipt
  // -------------------------------------------
  const handleGRN = async () => {
    try {
      const poIdNum = Number(poId);
      const supplierIdNum = Number(supplierId);
      const productIdNum = Number(productId);
      const orderedQtyNum = Number(orderedQuantity);
      const receivedQtyNum = Number(receivedQuantity);

      if (Number.isNaN(poIdNum) || Number.isNaN(supplierIdNum) || Number.isNaN(productIdNum)) {
        alert("PO ID, Supplier ID, and Product ID must be numeric (backend expects Long).");
        return;
      }
      if (Number.isNaN(orderedQtyNum) || orderedQtyNum < 1) {
        alert("Ordered quantity must be at least 1.");
        return;
      }
      if (Number.isNaN(receivedQtyNum) || receivedQtyNum < 0) {
        alert("Received quantity cannot be negative.");
        return;
      }

      const payload = {
        receiptNumber,
        poId: poIdNum,
        supplierId: supplierIdNum,
        receiptDate, // yyyy-MM-dd
        receivedBy,
        warehouseLocation,
        deliveryNoteNumber,
        vehicleNumber,
        notes: grnNotes,
        items: [
          {
            productId: productIdNum,
            productName,
            productSku,
            orderedQuantity: orderedQtyNum,
            receivedQuantity: receivedQtyNum,
            batchNumber,
            expiryDate, // yyyy-MM-dd
            notes: itemNotes,
          },
        ],
      };

      const res = await createGoodsReceipt(payload);
      // backend wraps in { success, message, data }
      const data = res.data || res; // depending on your request() wrapper
      setResult(data);

      if (data.receiptId != null) {
        setReceiptIdForInspection(String(data.receiptId));
      }

      alert("Goods receipt created successfully.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error creating goods receipt");
    }
  };

  // -------------------------------------------
  // Perform Inspection
  // -------------------------------------------
  const handleInspection = async () => {
    try {
      const receiptIdNum = Number(receiptIdForInspection);
      const inspectionIdNum = Number(inspectionId);
      const acceptedQtyNum = Number(acceptedQuantity);
      const rejectedQtyNum = Number(rejectedQuantity || 0);
      const damagedQtyNum = Number(damagedQuantity || 0);
      const ratingNum = Number(qualityRating);

      if (Number.isNaN(receiptIdNum) || Number.isNaN(inspectionIdNum)) {
        alert("Receipt ID and Inspection ID must be numeric (backend expects Long).");
        return;
      }
      if (Number.isNaN(acceptedQtyNum) || acceptedQtyNum < 0) {
        alert("Accepted quantity cannot be negative.");
        return;
      }
      if (Number.isNaN(rejectedQtyNum) || rejectedQtyNum < 0) {
        alert("Rejected quantity cannot be negative.");
        return;
      }
      if (Number.isNaN(damagedQtyNum) || damagedQtyNum < 0) {
        alert("Damaged quantity cannot be negative.");
        return;
      }
      if (Number.isNaN(ratingNum) || ratingNum < 1 || ratingNum > 5) {
        alert("Quality rating must be between 1 and 5.");
        return;
      }

      const payload = {
        receiptId: receiptIdNum,
        inspectionId: inspectionIdNum,
        acceptedQuantity: acceptedQtyNum,
        rejectedQuantity: rejectedQtyNum,
        damagedQuantity: damagedQtyNum,
        inspectorName,
        qualityRating: ratingNum,
        defectType,    // must be one of: DAMAGED|EXPIRED|WRONG_ITEM|POOR_QUALITY|PACKAGING_ISSUE|NONE
        actionTaken,   // must be one of: ACCEPT_ALL|PARTIAL_ACCEPT|REJECT_ALL|RETURN_TO_SUPPLIER
        inspectionNotes,
        photoUrl,
      };

      const res = await performInspection(payload);
      const data = res.data || res;
      setResult(data);

      alert("Inspection submitted successfully.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error performing inspection");
    }
  };

  return (
    <div className="page">
      <h2>Goods Receiving & Inspection</h2>

      <div className="card">
        <h3>Create Goods Receipt (GRN)</h3>

        <label>
          Receipt Number:
          <input
            value={receiptNumber}
            onChange={(e) => setReceiptNumber(e.target.value)}
          />
        </label>

        <label>
          PO ID (numeric):
          <input value={poId} onChange={(e) => setPoId(e.target.value)} />
        </label>

        <label>
          Supplier ID (numeric):
          <input
            value={supplierId}
            onChange={(e) => setSupplierId(e.target.value)}
          />
        </label>

        <label>
          Receipt Date:
          <input
            type="date"
            value={receiptDate}
            onChange={(e) => setReceiptDate(e.target.value)}
          />
        </label>

        <label>
          Received By:
          <input
            value={receivedBy}
            onChange={(e) => setReceivedBy(e.target.value)}
          />
        </label>

        <label>
          Warehouse Location:
          <input
            value={warehouseLocation}
            onChange={(e) => setWarehouseLocation(e.target.value)}
          />
        </label>

        <label>
          Delivery Note Number:
          <input
            value={deliveryNoteNumber}
            onChange={(e) => setDeliveryNoteNumber(e.target.value)}
          />
        </label>

        <label>
          Vehicle Number:
          <input
            value={vehicleNumber}
            onChange={(e) => setVehicleNumber(e.target.value)}
          />
        </label>

        <label>
          GRN Notes:
          <input
            value={grnNotes}
            onChange={(e) => setGrnNotes(e.target.value)}
          />
        </label>

        <h4>Line Item</h4>

        <label>
          Product ID (numeric):
          <input
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
          />
        </label>

        <label>
          Product Name:
          <input
            value={productName}
            onChange={(e) => setProductName(e.target.value)}
          />
        </label>

        <label>
          Product SKU:
          <input
            value={productSku}
            onChange={(e) => setProductSku(e.target.value)}
          />
        </label>

        <label>
          Ordered Quantity:
          <input
            type="number"
            value={orderedQuantity}
            onChange={(e) => setOrderedQuantity(e.target.value)}
          />
        </label>

        <label>
          Received Quantity:
          <input
            type="number"
            value={receivedQuantity}
            onChange={(e) => setReceivedQuantity(e.target.value)}
          />
        </label>

        <label>
          Batch Number:
          <input
            value={batchNumber}
            onChange={(e) => setBatchNumber(e.target.value)}
          />
        </label>

        <label>
          Expiry Date:
          <input
            type="date"
            value={expiryDate}
            onChange={(e) => setExpiryDate(e.target.value)}
          />
        </label>

        <label>
          Item Notes:
          <input
            value={itemNotes}
            onChange={(e) => setItemNotes(e.target.value)}
          />
        </label>
      </div>

      <div className="card">
        <h3>Perform Quality Inspection</h3>

        <label>
          Receipt ID (from GRN response):
          <input
            value={receiptIdForInspection}
            onChange={(e) => setReceiptIdForInspection(e.target.value)}
          />
        </label>

        <label>
          Inspection ID (numeric):
          <input
            value={inspectionId}
            onChange={(e) => setInspectionId(e.target.value)}
          />
        </label>

        <label>
          Accepted Quantity:
          <input
            type="number"
            value={acceptedQuantity}
            onChange={(e) => setAcceptedQuantity(e.target.value)}
          />
        </label>

        <label>
          Rejected Quantity:
          <input
            type="number"
            value={rejectedQuantity}
            onChange={(e) => setRejectedQuantity(e.target.value)}
          />
        </label>

        <label>
          Damaged Quantity:
          <input
            type="number"
            value={damagedQuantity}
            onChange={(e) => setDamagedQuantity(e.target.value)}
          />
        </label>

        <label>
          Inspector Name:
          <input
            value={inspectorName}
            onChange={(e) => setInspectorName(e.target.value)}
          />
        </label>

        <label>
          Quality Rating (1–5):
          <input
            type="number"
            value={qualityRating}
            onChange={(e) => setQualityRating(e.target.value)}
          />
        </label>

        <label>
          Defect Type:
          <select
            value={defectType}
            onChange={(e) => setDefectType(e.target.value)}
          >
            <option value="NONE">NONE</option>
            <option value="DAMAGED">DAMAGED</option>
            <option value="EXPIRED">EXPIRED</option>
            <option value="WRONG_ITEM">WRONG_ITEM</option>
            <option value="POOR_QUALITY">POOR_QUALITY</option>
            <option value="PACKAGING_ISSUE">PACKAGING_ISSUE</option>
          </select>
        </label>

        <label>
          Action Taken:
          <select
            value={actionTaken}
            onChange={(e) => setActionTaken(e.target.value)}
          >
            <option value="ACCEPT_ALL">ACCEPT_ALL</option>
            <option value="PARTIAL_ACCEPT">PARTIAL_ACCEPT</option>
            <option value="REJECT_ALL">REJECT_ALL</option>
            <option value="RETURN_TO_SUPPLIER">RETURN_TO_SUPPLIER</option>
          </select>
        </label>

        <label>
          Inspection Notes:
          <input
            value={inspectionNotes}
            onChange={(e) => setInspectionNotes(e.target.value)}
          />
        </label>

        <label>
          Photo URL:
          <input
            value={photoUrl}
            onChange={(e) => setPhotoUrl(e.target.value)}
          />
        </label>

        <div className="actions">
          <button onClick={handleGRN}>Create Goods Receipt</button>
          <button onClick={handleInspection}>Perform Inspection</button>
        </div>
      </div>

      {result && (
        <div className="card">
          <h3>API Response</h3>
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}
    </div>
  );
}
