// src/pages/ReconciliationReporting.jsx
import { useState } from "react";
import {
  createReconciliationReport,
  generateInventoryReport,
} from "../services/api";

export default function ReconciliationReporting() {
  const [sessionId, setSessionId] = useState("REC-001");
  const [warehouseId, setWarehouseId] = useState("WH-01");
  const [reportType, setReportType] = useState("DETAILED");
  const [result, setResult] = useState(null);

  const handleCreateReconciliation = async () => {
    try {
      const payload = { sessionId, warehouseId };
      const res = await createReconciliationReport(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error creating reconciliation report");
    }
  };

  const handleGenerateInventoryReport = async () => {
    try {
      const res = await generateInventoryReport(warehouseId, reportType);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error generating inventory report");
    }
  };

  return (
    <div className="page">
      <h2>Inventory Reconciliation & Reporting</h2>

      <div className="card">
        <h3>Reconciliation & Inventory Reports</h3>
        <label>Session ID:
          <input value={sessionId} onChange={e => setSessionId(e.target.value)} />
        </label>
        <label>Warehouse ID:
          <input value={warehouseId} onChange={e => setWarehouseId(e.target.value)} />
        </label>
        <label>Report Type:
          <input value={reportType} onChange={e => setReportType(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleCreateReconciliation}>Create Reconciliation Report</button>
          <button onClick={handleGenerateInventoryReport}>Generate Inventory Report</button>
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
