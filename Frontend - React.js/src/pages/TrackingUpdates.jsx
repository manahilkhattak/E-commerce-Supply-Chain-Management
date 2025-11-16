// src/pages/TrackingUpdates.jsx
import { useState } from "react";
import {
  addTrackingEvent,
  getTrackingStatus,
} from "../services/api";

export default function TrackingUpdates() {
  const [trackingNumber, setTrackingNumber] = useState("TRK-001");
  const [location, setLocation] = useState("Rawalpindi Hub");
  const [status, setStatus] = useState("In Transit");
  const [result, setResult] = useState(null);

  const handleAddEvent = async () => {
    try {
      const payload = {
        trackingNumber,
        location,
        status,
        eventTime: new Date().toISOString(),
      };
      const res = await addTrackingEvent(payload);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error adding tracking event");
    }
  };

  const handleGetStatus = async () => {
    try {
      const res = await getTrackingStatus(trackingNumber);
      setResult(res.data);
    } catch (e) {
      console.error(e);
      alert("Error getting tracking status");
    }
  };

  return (
    <div className="page">
      <h2>Tracking & Delivery Updates</h2>

      <div className="card">
        <h3>Tracking</h3>
        <label>Tracking Number:
          <input value={trackingNumber} onChange={e => setTrackingNumber(e.target.value)} />
        </label>
        <label>Location:
          <input value={location} onChange={e => setLocation(e.target.value)} />
        </label>
        <label>Status:
          <input value={status} onChange={e => setStatus(e.target.value)} />
        </label>

        <div className="actions">
          <button onClick={handleAddEvent}>Add Tracking Event</button>
          <button onClick={handleGetStatus}>Get Tracking Status</button>
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
