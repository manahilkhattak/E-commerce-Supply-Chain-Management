// src/pages/Dashboard.jsx
import React, { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line,
  Legend,
} from "recharts";
import { getProducts } from "../services/api";

// ------- SAMPLE DATA (fallbacks for charts) -------
const sampleInventoryData = [
  { name: "Item A", stock: 120 },
  { name: "Item B", stock: 80 },
  { name: "Item C", stock: 45 },
  { name: "Item D", stock: 200 },
];

const sampleOrderStatus = [
  { name: "Pending", value: 8 },
  { name: "Processing", value: 14 },
  { name: "Shipped", value: 20 },
  { name: "Delivered", value: 40 },
];

const sampleShipmentTimeline = [
  { date: "Mon", shipments: 4 },
  { date: "Tue", shipments: 6 },
  { date: "Wed", shipments: 3 },
  { date: "Thu", shipments: 8 },
  { date: "Fri", shipments: 5 },
];

const COLORS = ["#4ade80", "#60a5fa", "#f97316", "#facc15"];

export default function Dashboard() {
  // ✅ state for live inventory data from backend
  const [inventoryData, setInventoryData] = useState(sampleInventoryData);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function loadInventory() {
      try {
        setLoading(true);
        const products = await getProducts(); // GET /catalog/products

        const mapped = (products || []).map((p) => ({
          name: p.productName || `#${p.productId}`,
          stock: p.currentStock ?? 0,
        }));

        if (mapped.length > 0) {
          setInventoryData(mapped);
        }
      } catch (err) {
        console.error("Failed to load products for dashboard", err);
        // keep sample data if API fails
      } finally {
        setLoading(false);
      }
    }

    loadInventory();
  }, []);

  const totalItems = inventoryData.reduce(
    (sum, item) => sum + (item.stock || 0),
    0
  );

  const totalOrders = sampleOrderStatus.reduce(
    (sum, s) => sum + (s.value || 0),
    0
  );

  return (
    <div className="page">
      <h2>Executive Dashboard</h2>

      {/* Top summary cards */}
      <div className="dashboard-cards">
        <div className="card">
          <h3>Total Inventory Units</h3>
          <p className="stat">{totalItems}</p>
          {loading && <small>Loading from backend…</small>}
        </div>

        <div className="card">
          <h3>Total Orders</h3>
          <p className="stat">{totalOrders}</p>
          <small>Across all statuses</small>
        </div>

        <div className="card">
          <h3>Active Shipments</h3>
          <p className="stat">24</p>
          <small>Sample live metric</small>
        </div>

        <div className="card">
          <h3>Open Exceptions</h3>
          <p className="stat">3</p>
          <small>Critical delivery issues</small>
        </div>
      </div>

      {/* Charts row 1 */}
      <div className="dashboard-row">
        {/* Inventory levels bar chart (✅ uses real DB data) */}
        <div className="card chart-card">
          <h3>Inventory Levels by Product</h3>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart
              data={inventoryData}
              margin={{ top: 20, right: 20, left: 0, bottom: 40 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis
                dataKey="name"
                angle={-30}
                textAnchor="end"
                interval={0}
                height={60}
              />
              <YAxis />
              <Tooltip />
              <Bar dataKey="stock" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Order status pie chart (sample data) */}
        <div className="card chart-card">
          <h3>Order Status Distribution</h3>
          <ResponsiveContainer width="100%" height={250}>
            <PieChart>
              <Pie
                data={sampleOrderStatus}
                dataKey="value"
                nameKey="name"
                outerRadius={80}
                label
              >
                {sampleOrderStatus.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Charts row 2 */}
      <div className="dashboard-row">
        <div className="card chart-card">
          <h3>Shipment Timeline (Sample)</h3>
          <ResponsiveContainer width="100%" height={250}>
            <LineChart data={sampleShipmentTimeline} margin={{ top: 20, right: 20, left: 0, bottom: 20 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="shipments" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}
