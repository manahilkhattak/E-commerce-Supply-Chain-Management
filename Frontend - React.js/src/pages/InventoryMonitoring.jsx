// src/pages/InventoryMonitoring.jsx
import React, { useState } from "react";
import {
  addProductToInventoryMonitoring,
  updateInventoryStock,
  runInventoryHealthCheck,
  getActiveInventoryAlerts,
  resolveInventoryAlert,
} from "../services/api";

function InventoryMonitoring() {
  // Use numeric IDs – backend expects Long
  const [productId, setProductId] = useState(1);
  const [threshold, setThreshold] = useState(10);
  const [deltaQty, setDeltaQty] = useState(0);
  const [alertId, setAlertId] = useState("");

  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  // -------- Configure monitoring ----------
  const handleMonitor = async () => {
    try {
      setLoading(true);

      const payload = {
        productId: Number(productId),
        thresholdQuantity: Number(threshold),
        thresholdType: "REORDER_POINT",
      };

      const res = await addProductToInventoryMonitoring(payload);
      setResult(res);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to configure monitoring for this product");
    } finally {
      setLoading(false);
    }
  };

  // -------- Update stock ----------
  const handleStockUpdate = async () => {
    try {
      setLoading(true);

      const qty = Number(deltaQty);

      const payload = {
        productId: Number(productId),
        quantityChange: qty,
        referenceNumber: "MONITORING-UI-ADJUSTMENT",
        reason: "Manual stock update from monitoring page",
      };

      const res = await updateInventoryStock(payload);
      setResult(res);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to update stock");
    } finally {
      setLoading(false);
    }
  };

  // -------- Health check ----------
  const handleHealthCheck = async () => {
    try {
      setLoading(true);
      const res = await runInventoryHealthCheck();
      setResult(res);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to run inventory health check");
    } finally {
      setLoading(false);
    }
  };

  // -------- Get active alerts ----------
  const handleGetAlerts = async () => {
    try {
      setLoading(true);
      const res = await getActiveInventoryAlerts();
      setResult(res);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to fetch active inventory alerts");
    } finally {
      setLoading(false);
    }
  };

  // -------- Resolve alert ----------
  const handleResolve = async () => {
    try {
      setLoading(true);

      if (!alertId) {
        alert("Please enter a numeric Alert ID.");
        return;
      }

      const res = await resolveInventoryAlert(
        Number(alertId),
        "Resolved from monitoring UI"
      );
      setResult(res);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to resolve alert");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <h2>Inventory Monitoring & Stock Alerts</h2>

      <div className="card">
        <h3>Monitoring & Alerts</h3>

        <label>
          Product ID (DB numeric product_id):
          <input
            type="number"
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
          />
        </label>

        <label>
          Threshold (min stock / reorder point):
          <input
            type="number"
            value={threshold}
            onChange={(e) => setThreshold(e.target.value)}
          />
        </label>

        <label>
          Stock Change (Δ Quantity):
          <input
            type="number"
            value={deltaQty}
            onChange={(e) => setDeltaQty(e.target.value)}
          />
        </label>

        <label>
          Alert ID (numeric, for resolve):
          <input
            type="number"
            value={alertId}
            onChange={(e) => setAlertId(e.target.value)}
          />
        </label>

        <div className="button-row">
          <button onClick={handleMonitor} disabled={loading}>
            Configure Monitoring
          </button>
          <button onClick={handleStockUpdate} disabled={loading}>
            Update Stock
          </button>
          <button onClick={handleHealthCheck} disabled={loading}>
            Run Health Check
          </button>
          <button onClick={handleGetAlerts} disabled={loading}>
            Get Active Alerts
          </button>
          <button onClick={handleResolve} disabled={loading}>
            Resolve Alert
          </button>
        </div>
      </div>

{result && (
  <pre
    style={{
      marginTop: "1rem",
      padding: "1rem",
      background: "#f5f5f5",
      color: "#000",              
      borderRadius: 4,
      maxHeight: "400px",
      overflow: "auto",
      fontSize: "0.85rem",
    }}
  >
    {JSON.stringify(result, null, 2)}
  </pre>
)}

    </div>
  );
}

export default InventoryMonitoring;
