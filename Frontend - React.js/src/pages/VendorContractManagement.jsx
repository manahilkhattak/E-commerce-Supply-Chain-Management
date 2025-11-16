// src/pages/VendorContractManagement.jsx
import { useState } from "react";
import { createContract, addSlaToContract } from "../services/api";

export default function VendorContractManagement() {
  // ---- Contract fields (ContractDTO) ----
  const [contractNumber, setContractNumber] = useState("CON-2025-001");
  const [supplierId, setSupplierId] = useState("1"); // numeric supplier ID from backend
  const [contractTitle, setContractTitle] = useState("Annual Supply Contract");
  const [contractType, setContractType] = useState("SUPPLY"); // SUPPLY|SERVICE|DISTRIBUTION|MAINTENANCE|CONSULTING

  // Use future dates to satisfy @FutureOrPresent and @Future
  const [startDate, setStartDate] = useState("2030-01-01");
  const [endDate, setEndDate] = useState("2031-01-01");

  const [contractValue, setContractValue] = useState("100000");
  const [currency, setCurrency] = useState("USD"); // USD|EUR|GBP|PKR
  const [paymentTerms, setPaymentTerms] = useState("NET30");
  const [renewalTerms, setRenewalTerms] = useState(
    "Auto-renews annually with 60-day notice period."
  );
  const [notes, setNotes] = useState("");

  // Contract ID returned from backend (for SLA)
  const [contractId, setContractId] = useState("");

  // ---- SLA fields (SLACreationDTO) ----
  const [metricName, setMetricName] = useState("On-time delivery rate");
  const [metricDescription, setMetricDescription] = useState(
    "Percentage of purchase orders delivered on or before the agreed date."
  );
  const [targetValue, setTargetValue] = useState("99");
  const [measurementUnit, setMeasurementUnit] = useState("PERCENTAGE"); // PERCENTAGE|HOURS|DAYS|COUNT
  const [minimumAcceptable, setMinimumAcceptable] = useState("95");
  const [penaltyClause, setPenaltyClause] = useState(
    "Penalty applies if on-time delivery falls below 95% in a given month."
  );
  const [monitoringFrequency, setMonitoringFrequency] =
    useState("MONTHLY"); // DAILY|WEEKLY|MONTHLY|QUARTERLY

  const [result, setResult] = useState(null);

  // -----------------------------
  // Create Contract
  // -----------------------------
  const handleCreateContract = async () => {
    try {
      const supplierIdNum = Number(supplierId);
      if (Number.isNaN(supplierIdNum)) {
        alert("Supplier ID must be a numeric value (backend Long).");
        return;
      }

      const contractValueNum = Number(contractValue);
      if (Number.isNaN(contractValueNum) || contractValueNum <= 0) {
        alert("Contract value must be a positive number.");
        return;
      }

      const payload = {
        contractNumber,      // required
        supplierId: supplierIdNum, // required Long
        contractTitle,       // required, 5–255 chars
        contractType,        // SUPPLY|SERVICE|DISTRIBUTION|MAINTENANCE|CONSULTING
        startDate,           // yyyy-MM-dd, FutureOrPresent
        endDate,             // yyyy-MM-dd, Future
        contractValue: contractValueNum, // BigDecimal > 0
        currency,            // USD|EUR|GBP|PKR
        paymentTerms,        // required
        renewalTerms,        // optional
        notes,               // optional
      };

      const res = await createContract(payload);
      setResult(res.data);

      // Grab the numeric contractId from ContractResponseDTO
      const returned = res.data || {};
      if (returned.contractId != null) {
        setContractId(String(returned.contractId));
      }

      alert("Contract created successfully.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error creating contract");
    }
  };

  // -----------------------------
  // Add SLA to Contract
  // -----------------------------
  const handleAddSla = async () => {
    try {
      const contractIdNum = Number(contractId);
      if (Number.isNaN(contractIdNum)) {
        alert(
          "Contract ID must be numeric (use the ID returned from create contract)."
        );
        return;
      }

      const payload = {
        contractId: contractIdNum,
        metricName,
        metricDescription,
        targetValue,
        measurementUnit,
        minimumAcceptable,
        penaltyClause,
        monitoringFrequency,
      };

      const res = await addSlaToContract(payload);
      setResult(res.data);
      alert("SLA added to contract successfully.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error adding SLA");
    }
  };

  return (
    <div className="page">
      <h2>Vendor Contract Management</h2>

      {/* -------- Contract Creation -------- */}
      <div className="card">
        <h3>Create Contract</h3>

        <div className="form-grid">
          <label>
            Contract Number:
            <input
              type="text"
              value={contractNumber}
              onChange={(e) => setContractNumber(e.target.value)}
            />
          </label>

          <label>
            Supplier ID (numeric):
            <input
              type="text"
              value={supplierId}
              onChange={(e) => setSupplierId(e.target.value)}
            />
          </label>

          <label>
            Contract Title:
            <input
              type="text"
              value={contractTitle}
              onChange={(e) => setContractTitle(e.target.value)}
            />
          </label>

          <label>
            Contract Type:
            <select
              value={contractType}
              onChange={(e) => setContractType(e.target.value)}
            >
              <option value="SUPPLY">SUPPLY</option>
              <option value="SERVICE">SERVICE</option>
              <option value="DISTRIBUTION">DISTRIBUTION</option>
              <option value="MAINTENANCE">MAINTENANCE</option>
              <option value="CONSULTING">CONSULTING</option>
            </select>
          </label>

          <label>
            Start Date:
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </label>

          <label>
            End Date:
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </label>

          <label>
            Contract Value:
            <input
              type="number"
              value={contractValue}
              onChange={(e) => setContractValue(e.target.value)}
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
              type="text"
              value={paymentTerms}
              onChange={(e) => setPaymentTerms(e.target.value)}
              placeholder="NET30 / NET45 / NET60"
            />
          </label>

          <label>
            Renewal Terms:
            <input
              type="text"
              value={renewalTerms}
              onChange={(e) => setRenewalTerms(e.target.value)}
            />
          </label>

          <label>
            Notes:
            <input
              type="text"
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
            />
          </label>
        </div>

        <div className="actions">
          <button onClick={handleCreateContract}>Create Contract</button>
        </div>
      </div>

      {/* -------- SLA Creation -------- */}
      <div className="card">
        <h3>Add SLA to Contract</h3>

        <div className="form-grid">
          <label>
            Contract ID (from response):
            <input
              type="text"
              value={contractId}
              onChange={(e) => setContractId(e.target.value)}
            />
          </label>

          <label>
            Metric Name:
            <input
              type="text"
              value={metricName}
              onChange={(e) => setMetricName(e.target.value)}
            />
          </label>

          <label>
            Metric Description:
            <input
              type="text"
              value={metricDescription}
              onChange={(e) => setMetricDescription(e.target.value)}
            />
          </label>

          <label>
            Target Value:
            <input
              type="text"
              value={targetValue}
              onChange={(e) => setTargetValue(e.target.value)}
              placeholder="e.g. 99"
            />
          </label>

          <label>
            Measurement Unit:
            <select
              value={measurementUnit}
              onChange={(e) => setMeasurementUnit(e.target.value)}
            >
              <option value="PERCENTAGE">PERCENTAGE</option>
              <option value="HOURS">HOURS</option>
              <option value="DAYS">DAYS</option>
              <option value="COUNT">COUNT</option>
            </select>
          </label>

          <label>
            Minimum Acceptable:
            <input
              type="text"
              value={minimumAcceptable}
              onChange={(e) => setMinimumAcceptable(e.target.value)}
              placeholder="e.g. 95"
            />
          </label>

          <label>
            Penalty Clause:
            <input
              type="text"
              value={penaltyClause}
              onChange={(e) => setPenaltyClause(e.target.value)}
            />
          </label>

          <label>
            Monitoring Frequency:
            <select
              value={monitoringFrequency}
              onChange={(e) => setMonitoringFrequency(e.target.value)}
            >
              <option value="DAILY">DAILY</option>
              <option value="WEEKLY">WEEKLY</option>
              <option value="MONTHLY">MONTHLY</option>
              <option value="QUARTERLY">QUARTERLY</option>
            </select>
          </label>
        </div>

        <div className="actions">
          <button onClick={handleAddSla}>Add SLA to Contract</button>
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
