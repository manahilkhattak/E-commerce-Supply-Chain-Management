// src/pages/ReturnsProcessing.jsx
import { useState } from "react";
import {
  createReturnOrder,
  approveReturnOrder,
} from "../services/api";

export default function ReturnsProcessing() {
  const [returnId, setReturnId] = useState("RET-001");
  const [orderId, setOrderId] = useState("ORD-789456");
  const [reason, setReason] = useState("Customer not satisfied");
  const [approvedBy, setApprovedBy] = useState("MANAGER-02");
  const [result, setResult] = useState(null);

  const handleCreateReturn = async () => {
    try {
      const payload = { returnId, orderId, reason };
      const res = await createReturnOrder(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error creating return order");
    }
  };

  const handleApproveReturn = async () => {
    try {
      const res = await approveReturnOrder(returnId, approvedBy);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error approving return");
    }
  };

  return (
    <div className="page">
      <h2>Returns Processing & Restocking</h2>

      <div className="card">
        <h3>Return Orders</h3>
        <label>Return ID:
          <input value={returnId} onChange={e => setReturnId(e.target.value)} />
        </label>
        <label>Order ID:
          <input value={orderId} onChange={e => setOrderId(e.target.value)} />
        </label>
        <label>Reason:
          <textarea value={reason} onChange={e => setReason(e.target.value)} />
        </label>
        <label>Approved By:
          <input value={approvedBy} onChange={e => setApprovedBy(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleCreateReturn}>Create Return Order</button>
          <button onClick={handleApproveReturn}>Approve Return</button>
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
