// src/pages/WarehouseStorage.jsx
import { useState } from "react";
import {
  getAvailableStorageLocations,
  assignProductToShelf,
} from "../services/api";

export default function WarehouseStorage() {
  const [productType, setProductType] = useState("electronics");
  const [sizeCategory, setSizeCategory] = useState("medium");
  const [tempControl, setTempControl] = useState(false);
  const [locations, setLocations] = useState([]);
  const [productId, setProductId] = useState("PROD-12345");
  const [locationId, setLocationId] = useState("A-12-3");
  const [result, setResult] = useState(null);

  const handleGetLocations = async () => {
    try {
      const params = {
        productType,
        sizeCategory,
        temperatureControl: tempControl,
      };
      const res = await getAvailableStorageLocations(params);
      setLocations(res.data);
    } catch (e) {
      console.error(e);
      alert("Error fetching locations");
    }
  };

  const handleAssign = async () => {
    try {
      const payload = {
        productId,
        quantity: 100,
        locationId,
        batchNumber: "BATCH-2025-01",
        expiryDate: "2026-01-01",
        placementStrategy: "FIFO",
        operatorId: "EMP-101",
        qualityCheckPassed: true,
        specialHandling: "Fragile",
      };
      const res = await assignProductToShelf(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error assigning shelf");
    }
  };

  return (
    <div className="page">
      <h2>Warehouse Storage & Shelf Placement</h2>

      <div className="card">
        <h3>Find & Assign Storage</h3>
        <label>Product Type:
          <input value={productType} onChange={e => setProductType(e.target.value)} />
        </label>
        <label>Size Category:
          <input value={sizeCategory} onChange={e => setSizeCategory(e.target.value)} />
        </label>
        <label>Temperature Control:
          <select value={tempControl ? "true" : "false"}
                  onChange={e => setTempControl(e.target.value === "true")}>
            <option value="false">No</option>
            <option value="true">Yes</option>
          </select>
        </label>

        <div className="actions">
          <button onClick={handleGetLocations}>Get Available Locations</button>
        </div>

        <hr />

        <label>Product ID:
          <input value={productId} onChange={e => setProductId(e.target.value)} />
        </label>
        <label>Location ID:
          <input value={locationId} onChange={e => setLocationId(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleAssign}>Assign Product to Shelf</button>
        </div>
      </div>

      {locations.length > 0 && (
        <div className="card">
          <h3>Locations</h3>
          <pre>{JSON.stringify(locations, null, 2)}</pre>
        </div>
      )}

      {result && (
        <div className="card">
          <h3>Assignment Response</h3>
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}
    </div>
  );
}
