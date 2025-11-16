// src/pages/DemandForecasting.jsx
import { useState } from "react";
import {
  getDemandForecast,      // GET forecast (if you have this)
  generateReorderPlan,    // POST reorder plan
} from "../services/api";

export default function DemandForecasting() {
  const [productId, setProductId] = useState(1);
  const [horizonDays, setHorizonDays] = useState(30);
  const [avgDailyDemand, setAvgDailyDemand] = useState(10);
  const [leadTimeDays, setLeadTimeDays] = useState(7);
  const [safetyStock, setSafetyStock] = useState(20);

  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleForecast = async () => {
    try {
      setLoading(true);
      const res = await getDemandForecast(
        Number(productId),
        Number(horizonDays)
      );
      setResult(res);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to get demand forecast");
    } finally {
      setLoading(false);
    }
  };

  const handlePlan = async () => {
    try {
      setLoading(true);

      const payload = {
        productId: Number(productId),
        forecastHorizonDays: Number(horizonDays),
        averageDailyDemand: Number(avgDailyDemand),
        leadTimeDays: Number(leadTimeDays),
        safetyStock: Number(safetyStock),
      };

      const res = await generateReorderPlan(payload);
      setResult(res);
    } catch (err) {
      console.error(err);
      alert(err.message || "Failed to generate reorder plan");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <h2>Demand Forecasting & Reorder Planning</h2>

      <div className="card">
        <h3>Forecast Inputs</h3>

        <label>
          Product ID (DB numeric product_id):
          <input
            type="number"
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
          />
        </label>

        <label>
          Forecast horizon (days):
          <input
            type="number"
            value={horizonDays}
            onChange={(e) => setHorizonDays(e.target.value)}
          />
        </label>

        <label>
          Average daily demand (units/day):
          <input
            type="number"
            value={avgDailyDemand}
            onChange={(e) => setAvgDailyDemand(e.target.value)}
          />
        </label>

        <label>
          Lead time (days):
          <input
            type="number"
            value={leadTimeDays}
            onChange={(e) => setLeadTimeDays(e.target.value)}
          />
        </label>

        <label>
          Safety stock (units):
          <input
            type="number"
            value={safetyStock}
            onChange={(e) => setSafetyStock(e.target.value)}
          />
        </label>

        <div className="button-row">
          <button onClick={handleForecast} disabled={loading}>
            Get Forecast
          </button>
          <button onClick={handlePlan} disabled={loading}>
            Generate Reorder Plan
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
