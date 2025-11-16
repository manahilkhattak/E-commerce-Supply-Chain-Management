// src/pages/Procurement.jsx
import { useState } from "react";
import { createPurchaseOrder, approvePurchaseOrder } from "../services/api";

export default function Procurement() {
  // --- PO header fields (PurchaseOrderDTO) ---
  const [poNumber, setPoNumber] = useState("PO-2025-001");
  const [supplierId, setSupplierId] = useState("1"); // numeric Long
  const [contractId, setContractId] = useState(""); // optional numeric
  const [expectedDeliveryDate, setExpectedDeliveryDate] = useState(
    "2030-01-10"
  );
  const [currency, setCurrency] = useState("USD"); // USD|EUR|GBP|PKR
  const [paymentTerms, setPaymentTerms] = useState("NET30");
  const [deliveryAddress, setDeliveryAddress] = useState(
    "123 Main Street, Sample City"
  );
  const [requestedBy, setRequestedBy] = useState("John Manager");
  const [headerNotes, setHeaderNotes] = useState("");

  // --- Single line item fields (PurchaseOrderItemDTO) ---
  const [productId, setProductId] = useState("1"); // numeric or blank
  const [productName, setProductName] = useState("Sample Product");
  const [productSku, setProductSku] = useState("SKU-001");
  const [quantity, setQuantity] = useState("10");
  const [unitPrice, setUnitPrice] = useState("50");
  const [taxRate, setTaxRate] = useState("0");
  const [discountRate, setDiscountRate] = useState("0");
  const [unitOfMeasurement, setUnitOfMeasurement] = useState("PIECE"); // PIECE|KG|LITER|BOX|METER|SET
  const [itemNotes, setItemNotes] = useState("");

  // --- Approval-related ---
  const [poId, setPoId] = useState(""); // numeric DB ID from response
  const [approvedBy, setApprovedBy] = useState("MANAGER-01");

  const [result, setResult] = useState(null);

  // -----------------------------
  // Create Purchase Order
  // -----------------------------
  const handleCreatePO = async () => {
    try {
      const supplierIdNum = Number(supplierId);
      if (Number.isNaN(supplierIdNum)) {
        alert("Supplier ID must be numeric (backend Long).");
        return;
      }

      const contractIdNum =
        contractId.trim() === "" ? null : Number(contractId);
      if (contractId.trim() !== "" && Number.isNaN(contractIdNum)) {
        alert("Contract ID must be numeric if provided.");
        return;
      }

      const quantityNum = Number(quantity);
      if (Number.isNaN(quantityNum) || quantityNum < 1) {
        alert("Quantity must be at least 1.");
        return;
      }

      const unitPriceNum = Number(unitPrice);
      if (Number.isNaN(unitPriceNum) || unitPriceNum <= 0) {
        alert("Unit price must be greater than 0.");
        return;
      }

      const taxRateNum =
        taxRate === "" ? 0 : Number(taxRate);
      const discountRateNum =
        discountRate === "" ? 0 : Number(discountRate);

      if (Number.isNaN(taxRateNum) || Number.isNaN(discountRateNum)) {
        alert("Tax rate and discount rate must be numeric.");
        return;
      }

      const productIdNum =
        productId.trim() === "" ? null : Number(productId);
      if (productId.trim() !== "" && Number.isNaN(productIdNum)) {
        alert("Product ID must be numeric if provided.");
        return;
      }

      // Build PurchaseOrderDTO payload
      const payload = {
        poNumber, // required
        supplierId: supplierIdNum, // required Long
        contractId: contractIdNum, // optional Long
        expectedDeliveryDate, // yyyy-MM-dd, @FutureOrPresent
        // you can also compute taxAmount/discountAmount here if you want
        taxAmount: null, // let service treat null as 0
        discountAmount: null,
        currency,
        paymentTerms,
        deliveryAddress,
        requestedBy,
        notes: headerNotes,
        items: [
          {
            productId: productIdNum, // optional
            productName,
            productSku,
            quantity: quantityNum,
            unitPrice: unitPriceNum,
            taxRate: taxRateNum,
            discountRate: discountRateNum,
            unitOfMeasurement,
            notes: itemNotes,
          },
        ],
      };

      const res = await createPurchaseOrder(payload);
      setResult(res.data);

      const returned = res.data || {};
      if (returned.poId != null) {
        // numeric PO ID from PurchaseOrderResponseDTO
        setPoId(String(returned.poId));
      }

      alert("Purchase order created successfully.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error creating purchase order");
    }
  };

  // -----------------------------
  // Approve Purchase Order
  // -----------------------------
  const handleApprovePO = async () => {
    try {
      const poIdNum = Number(poId);
      if (Number.isNaN(poIdNum)) {
        alert(
          "PO ID must be numeric. Use the ID returned from the create PO response (poId)."
        );
        return;
      }

      const res = await approvePurchaseOrder(poIdNum, approvedBy);
      setResult(res.data);
      alert("Purchase order approved successfully.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error approving purchase order");
    }
  };

  return (
    <div className="page">
      <h2>Procurement & Purchase Orders</h2>

      <div className="card">
        <h3>Create & Approve Purchase Order</h3>

        {/* --- PO Header --- */}
        <label>
          PO Number:
          <input
            value={poNumber}
            onChange={(e) => setPoNumber(e.target.value)}
          />
        </label>

        <label>
          Supplier ID (numeric):
          <input
            value={supplierId}
            onChange={(e) => setSupplierId(e.target.value)}
          />
        </label>

        <label>
          Contract ID (optional, numeric):
          <input
            value={contractId}
            onChange={(e) => setContractId(e.target.value)}
          />
        </label>

        <label>
          Expected Delivery Date:
          <input
            type="date"
            value={expectedDeliveryDate}
            onChange={(e) => setExpectedDeliveryDate(e.target.value)}
          />
        </label>

        <label>
          Currency:
          <select
            value={currency}
            onChange={(e) => setCurrency(e.target.value)}
          >
            <option value="USD">USD</option>
            <option value="EUR">EUR</option>
            <option value="GBP">GBP</option>
            <option value="PKR">PKR</option>
          </select>
        </label>

        <label>
          Payment Terms:
          <input
            value={paymentTerms}
            onChange={(e) => setPaymentTerms(e.target.value)}
          />
        </label>

        <label>
          Delivery Address:
          <input
            value={deliveryAddress}
            onChange={(e) => setDeliveryAddress(e.target.value)}
          />
        </label>

        <label>
          Requested By:
          <input
            value={requestedBy}
            onChange={(e) => setRequestedBy(e.target.value)}
          />
        </label>

        <label>
          Header Notes:
          <input
            value={headerNotes}
            onChange={(e) => setHeaderNotes(e.target.value)}
          />
        </label>

        {/* --- Single Item --- */}
        <h4>Line Item</h4>

        <label>
          Product ID (optional, numeric):
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
          Quantity:
          <input
            type="number"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
          />
        </label>

        <label>
          Unit Price:
          <input
            type="number"
            value={unitPrice}
            onChange={(e) => setUnitPrice(e.target.value)}
          />
        </label>

        <label>
          Tax Rate:
          <input
            type="number"
            value={taxRate}
            onChange={(e) => setTaxRate(e.target.value)}
          />
        </label>

        <label>
          Discount Rate:
          <input
            type="number"
            value={discountRate}
            onChange={(e) => setDiscountRate(e.target.value)}
          />
        </label>

        <label>
          Unit of Measurement:
          <select
            value={unitOfMeasurement}
            onChange={(e) => setUnitOfMeasurement(e.target.value)}
          >
            <option value="PIECE">PIECE</option>
            <option value="KG">KG</option>
            <option value="LITER">LITER</option>
            <option value="BOX">BOX</option>
            <option value="METER">METER</option>
            <option value="SET">SET</option>
          </select>
        </label>

        <label>
          Item Notes:
          <input
            value={itemNotes}
            onChange={(e) => setItemNotes(e.target.value)}
          />
        </label>

        {/* --- Approval --- */}
        <h4>Approval</h4>

        <label>
          PO ID (numeric from response):
          <input
            value={poId}
            onChange={(e) => setPoId(e.target.value)}
          />
        </label>

        <label>
          Approved By:
          <input
            value={approvedBy}
            onChange={(e) => setApprovedBy(e.target.value)}
          />
        </label>

        <div className="actions">
          <button onClick={handleCreatePO}>Create Purchase Order</button>
          <button onClick={handleApprovePO}>Approve Purchase Order</button>
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
