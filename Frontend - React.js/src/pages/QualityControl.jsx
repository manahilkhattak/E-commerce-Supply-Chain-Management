// src/pages/QualityControl.jsx
import { useState } from "react";
import {
  createQualityCheck,
  submitQualityCheckResults,
} from "../services/api";

export default function QualityControl() {
  const [checkId, setCheckId] = useState("QC-001");
  const [orderId, setOrderId] = useState("ORD-789456");
  const [status, setStatus] = useState("PASSED");
  const [remarks, setRemarks] = useState("All items verified.");
  const [result, setResult] = useState(null);

  const handleCreateCheck = async () => {
    try {
      const payload = { checkId, orderId };
      const res = await createQualityCheck(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error creating quality check");
    }
  };

  const handleSubmitResults = async () => {
    try {
      const payload = { checkId, status, remarks };
      const res = await submitQualityCheckResults(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error submitting results");
    }
  };

  return (
    <div className="page">
      <h2>Quality Control Before Shipment</h2>

      <div className="card">
        <h3>Quality Check</h3>
        <label>Check ID:
          <input value={checkId} onChange={e => setCheckId(e.target.value)} />
        </label>
        <label>Order ID:
          <input value={orderId} onChange={e => setOrderId(e.target.value)} />
        </label>
        <label>Status:
          <input value={status} onChange={e => setStatus(e.target.value)} />
        </label>
        <label>Remarks:
          <textarea value={remarks} onChange={e => setRemarks(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleCreateCheck}>Create Quality Check</button>
          <button onClick={handleSubmitResults}>Submit QC Results</button>
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
