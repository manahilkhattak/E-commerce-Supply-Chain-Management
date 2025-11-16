// src/pages/ShipmentScheduling.jsx
import { useState } from "react";
import {
  createShipment,
  scheduleShipmentDispatch,
} from "../services/api";

export default function ShipmentScheduling() {
  const [shipmentId, setShipmentId] = useState("SHIP-001");
  const [destination, setDestination] = useState("Rawalpindi");
  const [dispatchTime, setDispatchTime] = useState("2024-11-25T10:00:00");
  const [result, setResult] = useState(null);

  const handleCreateShipment = async () => {
    try {
      const payload = { shipmentId, destination };
      const res = await createShipment(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error creating shipment");
    }
  };

  const handleScheduleDispatch = async () => {
    try {
      const payload = { shipmentId, dispatchTime };
      const res = await scheduleShipmentDispatch(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error scheduling dispatch");
    }
  };

  return (
    <div className="page">
      <h2>Shipment Scheduling & Dispatch</h2>

      <div className="card">
        <h3>Shipment & Dispatch</h3>
        <label>Shipment ID:
          <input value={shipmentId} onChange={e => setShipmentId(e.target.value)} />
        </label>
        <label>Destination:
          <input value={destination} onChange={e => setDestination(e.target.value)} />
        </label>
        <label>Dispatch Time:
          <input value={dispatchTime} onChange={e => setDispatchTime(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleCreateShipment}>Create Shipment</button>
          <button onClick={handleScheduleDispatch}>Schedule Dispatch</button>
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
