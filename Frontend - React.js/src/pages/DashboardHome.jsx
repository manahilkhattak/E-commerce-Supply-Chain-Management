// src/pages/Dashboard.jsx
import { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Legend,
  Cell,
} from "recharts";

import { getProducts, getOrders } from "../services/api";

const ORDER_STATUS_COLORS = {
  PENDING: "#FFB020",
  PROCESSING: "#1E88E5",
  SHIPPED: "#43A047",
  DELIVERED: "#4CAF50",
  CANCELLED: "#E53935",
};

export default function Dashboard() {
  const [inventoryData, setInventoryData] = useState([]);
  const [orderStatusData, setOrderStatusData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");

  useEffect(() => {
    async function loadData() {
      try {
        setLoading(true);
        setErrorMsg("");

        // Fetch products and orders from your existing API helpers
        const products = await getProducts();
        const orders = await getOrders();

        // ---------- Build Inventory Chart Data ----------
        // Show top 10 products by stock to keep chart readable
        const inv = (Array.isArray(products) ? products : [])
          .map((p) => ({
            name: p.productName || p.product_name || `#${p.productId}`,
            stock:
              typeof p.currentStock === "number"
                ? p.currentStock
                : typeof p.current_stock === "number"
                ? p.current_stock
                : 0,
          }))
          .sort((a, b) => b.stock - a.stock)
          .slice(0, 10);

        setInventoryData(inv);

        // ---------- Build Order Status Pie Data ----------
        const counts = {};
        (Array.isArray(orders) ? orders : []).forEach((o) => {
          const raw =
            o.status ||
            o.orderStatus ||
            (typeof o.order_status === "string" ? o.order_status : "UNKNOWN");
          const key = String(raw).toUpperCase();
          counts[key] = (counts[key] || 0) + 1;
        });

        const statusData = Object.entries(counts).map(([status, value]) => ({
          name: status,
          value,
        }));

        setOrderStatusData(statusData);
      } catch (err) {
        console.error(err);
        setErrorMsg(err.message || "Failed to load dashboard data");
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, []);

  return (
    <div className="page">
      <h2>Supply Chain Dashboard</h2>

      {loading && <p>Loading dashboard data...</p>}
      {errorMsg && (
        <p style={{ color: "#ff6b6b", marginBottom: "1rem" }}>{errorMsg}</p>
      )}

      {/* Top level stats can be added here later if you want cards */}

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr",
          gap: "1.5rem",
          marginTop: "1rem",
        }}
      >
        {/* -------- Inventory Levels Bar Chart -------- */}
        <div
          style={{
            background: "#111827",
            borderRadius: 8,
            padding: "1rem",
            border: "1px solid #1f2937",
          }}
        >
          <h3 style={{ marginBottom: "0.75rem" }}>Inventory Levels (Top 10)</h3>
          {inventoryData.length === 0 ? (
            <p style={{ fontSize: "0.9rem" }}>No inventory data available.</p>
          ) : (
            <div style={{ width: "100%", height: 320 }}>
              <ResponsiveContainer>
                <BarChart data={inventoryData}>
                  <XAxis dataKey="name" hide />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="stock" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          )}
        </div>

        {/* -------- Order Status Pie Chart -------- */}
        <div
          style={{
            background: "#111827",
            borderRadius: 8,
            padding: "1rem",
            border: "1px solid #1f2937",
          }}
        >
          <h3 style={{ marginBottom: "0.75rem" }}>Order Status Distribution</h3>
          {orderStatusData.length === 0 ? (
            <p style={{ fontSize: "0.9rem" }}>No order data available.</p>
          ) : (
            <div style={{ width: "100%", height: 320 }}>
              <ResponsiveContainer>
                <PieChart>
                  <Pie
                    data={orderStatusData}
                    dataKey="value"
                    nameKey="name"
                    outerRadius={110}
                    label
                  >
                    {orderStatusData.map((entry, index) => {
                      const color =
                        ORDER_STATUS_COLORS[entry.name] ||
                        Object.values(ORDER_STATUS_COLORS)[
                          index % Object.values(ORDER_STATUS_COLORS).length
                        ];
                      return <Cell key={entry.name} fill={color} />;
                    })}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
