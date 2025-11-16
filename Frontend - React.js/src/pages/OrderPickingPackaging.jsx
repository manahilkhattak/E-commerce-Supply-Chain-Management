// src/pages/OrderPickingPackaging.jsx
import { useState } from "react";
import {
  generatePickingList,
  updatePickingStatus,
  createPackaging,
  completePackaging,
} from "../services/api";

export default function OrderPickingPackaging() {
  const [orderId, setOrderId] = useState("ORD-789456");
  const [pickingListId, setPickingListId] = useState("");
  const [packageId, setPackageId] = useState("");
  const [result, setResult] = useState(null);

  const handleGeneratePickingList = async () => {
    try {
      const payload = {
        orderId,
        priority: "STANDARD",
        items: [{ productId: "PROD-12345", quantity: 2, sku: "SKU-12345" }],
        pickingStrategy: "SHORTEST_PATH",
        maxPickingTime: 30,
      };
      const res = await generatePickingList(payload);
      setResult(res.data);
      if (res.data.pickingListId) setPickingListId(res.data.pickingListId);
    } catch (e) {
      console.error(e);
      alert("Error generating picking list");
    }
  };

  const handleUpdatePickingStatus = async () => {
    try {
      const payload = {
        pickingListId,
        itemsPicked: [{
          productId: "PROD-12345",
          quantityPicked: 2,
          locationId: "A-12-3",
          pickedAt: new Date().toISOString(),
          verificationCode: "VER-123",
        }],
        status: "IN_PROGRESS",
        pickerId: "PICKER-101",
        currentLocation: "A-12",
        issues: [],
      };
      const res = await updatePickingStatus(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error updating picking status");
    }
  };

  const handleCreatePackaging = async () => {
    try {
      const payload = {
        pickingListId,
        orderId,
        packageType: "STANDARD_BOX",
        fragileItems: true,
        giftWrap: false,
        customMessage: null,
        sustainablePackaging: true,
        insuranceRequired: false,
        declaredValue: 150,
      };
      const res = await createPackaging(payload);
      setResult(res.data);
      if (res.data.packageId) setPackageId(res.data.packageId);
    } catch (e) {
      console.error(e);
      alert("Error creating packaging");
    }
  };

  const handleCompletePackaging = async () => {
    try {
      const payload = {
        packageId,
        actualWeight: 1.45,
        dimensions: { length: 30, width: 20, height: 15, unit: "cm" },
        sealedBy: "PACKER-202",
        qualityCheckPassed: true,
        photosUrls: [],
        packingMaterialsUsed: { bubbleWrap: 2, recyclableFill: 100 },
      };
      const res = await completePackaging(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error completing packaging");
    }
  };

  return (
    <div className="page">
      <h2>Order Picking & Packaging</h2>

      <div className="card">
        <h3>Picking & Packaging Flow</h3>
        <label>Order ID:
          <input value={orderId} onChange={e => setOrderId(e.target.value)} />
        </label>
        <label>Picking List ID:
          <input value={pickingListId} onChange={e => setPickingListId(e.target.value)} />
        </label>
        <label>Package ID:
          <input value={packageId} onChange={e => setPackageId(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleGeneratePickingList}>Generate Picking List</button>
          <button onClick={handleUpdatePickingStatus} disabled={!pickingListId}>Update Picking Status</button>
          <button onClick={handleCreatePackaging} disabled={!pickingListId}>Create Packaging</button>
          <button onClick={handleCompletePackaging} disabled={!packageId}>Complete Packaging</button>
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
