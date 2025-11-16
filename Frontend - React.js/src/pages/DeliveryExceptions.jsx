// src/pages/DeliveryExceptions.jsx
import { useState } from "react";
import {
  createDeliveryException,
  resolveDeliveryException,
} from "../services/api";

export default function DeliveryExceptions() {
  const [exceptionId, setExceptionId] = useState("EX-001");
  const [orderId, setOrderId] = useState("ORD-789456");
  const [reason, setReason] = useState("Address not found");
  const [resolution, setResolution] = useState("Contacted customer and updated address");
  const [result, setResult] = useState(null);

  const handleCreate = async () => {
    try {
      const payload = { exceptionId, orderId, reason };
      const res = await createDeliveryException(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error creating delivery exception");
    }
  };

  const handleResolve = async () => {
    try {
      const payload = { exceptionId, resolution };
      const res = await resolveDeliveryException(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error resolving exception");
    }
  };

  return (
    <div className="page">
      <h2>Delivery Exceptions</h2>

      <div className="card">
        <h3>Manage Exceptions</h3>
        <label>Exception ID:
          <input value={exceptionId} onChange={e => setExceptionId(e.target.value)} />
        </label>
        <label>Order ID:
          <input value={orderId} onChange={e => setOrderId(e.target.value)} />
        </label>
        <label>Reason:
          <textarea value={reason} onChange={e => setReason(e.target.value)} />
        </label>
        <label>Resolution:
          <textarea value={resolution} onChange={e => setResolution(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleCreate}>Create Exception</button>
          <button onClick={handleResolve}>Resolve Exception</button>
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
