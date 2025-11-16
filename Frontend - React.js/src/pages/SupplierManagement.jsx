// src/pages/SupplierManagement.jsx
import { useState } from "react";
import { registerSupplier, updateSupplierApprovalStatus } from "../services/api";

export default function SupplierManagement() {
  // Supplier registration fields (match SupplierRegistrationDTO)
  const [companyName, setCompanyName] = useState("ABC Traders");
  const [contactPerson, setContactPerson] = useState("John Doe");
  const [email, setEmail] = useState("supplier@example.com");
  const [phone, setPhone] = useState("+12345678901");
  const [address, setAddress] = useState("123 Main Street");
  const [city, setCity] = useState("Sample City");
  const [country, setCountry] = useState("USA");
  const [taxId, setTaxId] = useState("TAX-12345");
  const [businessLicense, setBusinessLicense] = useState("LIC-98765");
  const [paymentTerms, setPaymentTerms] = useState("NET30");
  const [notes, setNotes] = useState("");

  // For approval/update (supplierId comes from backend after registration)
  const [supplierId, setSupplierId] = useState("");
  const [status, setStatus] = useState("APPROVED"); // APPROVED | REJECTED

  const [result, setResult] = useState(null);

  // -----------------------------
  // Register Supplier
  // -----------------------------
  const handleRegister = async () => {
    try {
      const payload = {
        companyName,
        contactPerson,
        email,
        phone,
        address,
        city,
        country,
        taxId,
        businessLicense,
        paymentTerms,
        notes,
      };

      const res = await registerSupplier(payload);
      // Backend response shape: { success, message, data: SupplierResponseDTO }
      setResult(res.data);

      // If registration succeeded, use returned supplierId for approval calls
      if (res.data && res.data.supplierId != null) {
        setSupplierId(String(res.data.supplierId));
      }

      alert("Supplier registered successfully.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error registering supplier");
    }
  };

  // -----------------------------
  // Approve / Reject Supplier
  // -----------------------------
  const handleApprovalUpdate = async () => {
    if (!supplierId) {
      alert("Please provide a numeric Supplier ID (or register a supplier first).");
      return;
    }

    try {
      const supplierIdNum = Number(supplierId);
      if (Number.isNaN(supplierIdNum)) {
        alert("Supplier ID must be a number (matches the backend Long ID).");
        return;
      }

      // SupplierApprovalDTO: { status: 'APPROVED' | 'REJECTED', notes: string }
      const payload = {
        status,
        notes: notes || `Status changed to ${status} from UI`,
      };

      const res = await updateSupplierApprovalStatus(supplierIdNum, payload);
      setResult(res.data);
      alert("Supplier approval status updated.");
    } catch (e) {
      console.error(e);
      alert(e.message || "Error updating approval status");
    }
  };

  return (
    <div className="page">
      <h2>Supplier Management</h2>

      <div className="card">
        <h3>Register Supplier</h3>

        <div className="form-grid">
          <label>
            Company Name:
            <input
              type="text"
              value={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
            />
          </label>

          <label>
            Contact Person:
            <input
              type="text"
              value={contactPerson}
              onChange={(e) => setContactPerson(e.target.value)}
            />
          </label>

          <label>
            Email:
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </label>

          <label>
            Phone:
            <input
              type="text"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />
          </label>

          <label>
            Address:
            <input
              type="text"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
            />
          </label>

          <label>
            City:
            <input
              type="text"
              value={city}
              onChange={(e) => setCity(e.target.value)}
            />
          </label>

          <label>
            Country:
            <input
              type="text"
              value={country}
              onChange={(e) => setCountry(e.target.value)}
            />
          </label>

          <label>
            Tax ID:
            <input
              type="text"
              value={taxId}
              onChange={(e) => setTaxId(e.target.value)}
            />
          </label>

          <label>
            Business License:
            <input
              type="text"
              value={businessLicense}
              onChange={(e) => setBusinessLicense(e.target.value)}
            />
          </label>

          <label>
            Payment Terms:
            <input
              type="text"
              value={paymentTerms}
              onChange={(e) => setPaymentTerms(e.target.value)}
            />
          </label>

          <label>
            Notes (optional):
            <input
              type="text"
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
            />
          </label>
        </div>

        <div className="actions">
          <button onClick={handleRegister}>Register Supplier</button>
        </div>
      </div>

      <div className="card">
        <h3>Approve / Reject Supplier</h3>
        <div className="form-grid">
          <label>
            Supplier ID (numeric from backend):
            <input
              type="text"
              value={supplierId}
              onChange={(e) => setSupplierId(e.target.value)}
            />
          </label>

          <label>
            Status:
            <select value={status} onChange={(e) => setStatus(e.target.value)}>
              <option value="APPROVED">APPROVED</option>
              <option value="REJECTED">REJECTED</option>
            </select>
          </label>
        </div>

        <div className="actions">
          <button onClick={handleApprovalUpdate}>Update Approval Status</button>
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
