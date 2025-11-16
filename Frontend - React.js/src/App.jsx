import { Routes, Route, NavLink, Navigate } from "react-router-dom";
import Dashboard from "./pages/Dashboard";                    // ✅ NEW
import SupplierManagement from "./pages/SupplierManagement";
import VendorContractManagement from "./pages/VendorContractManagement";
import Procurement from "./pages/Procurement";
import GoodsReceivingInspection from "./pages/GoodsReceivingInspection";
import CatalogStockManagement from "./pages/CatalogStockManagement";
import InventoryMonitoring from "./pages/InventoryMonitoring";
import DemandForecasting from "./pages/DemandForecasting";
import WarehouseStorage from "./pages/WarehouseStorage";
import OrderPickingPackaging from "./pages/OrderPickingPackaging";
import QualityControl from "./pages/QualityControl";
import ShipmentScheduling from "./pages/ShipmentScheduling";
import TrackingUpdates from "./pages/TrackingUpdates";
import DeliveryExceptions from "./pages/DeliveryExceptions";
import ReturnsProcessing from "./pages/ReturnsProcessing";
import ReconciliationReporting from "./pages/ReconciliationReporting";

function App() {
  return (
    <div className="app">
      <header className="navbar">
        <div className="brand">Inventory & Supply Chain EIS</div>
        <nav className="navlinks">
          {/* ✅ New Dashboard link */}
          <NavLink to="/dashboard">Dashboard</NavLink>

          <NavLink to="/suppliers">Suppliers</NavLink>
          <NavLink to="/contracts">Contracts</NavLink>
          <NavLink to="/procurement">Procurement</NavLink>
          <NavLink to="/receiving">Receiving</NavLink>
          <NavLink to="/catalog">Catalog</NavLink>
          <NavLink to="/monitoring">Monitoring</NavLink>
          <NavLink to="/forecasting">Forecasting</NavLink>
          <NavLink to="/storage">Storage</NavLink>
          <NavLink to="/picking">Picking</NavLink>
          <NavLink to="/quality">Quality</NavLink>
          <NavLink to="/shipments">Shipments</NavLink>
          <NavLink to="/tracking">Tracking</NavLink>
          <NavLink to="/exceptions">Exceptions</NavLink>
          <NavLink to="/returns">Returns</NavLink>
          <NavLink to="/reconciliation">Reconciliation</NavLink>
        </nav>
      </header>

      <main className="container">
        <Routes>
          {/* ✅ Default route -> Dashboard with charts */}
          <Route path="/" element={<Navigate to="/dashboard" replace />} />

          {/* ✅ Dashboard route */}
          <Route path="/dashboard" element={<Dashboard />} />

          <Route path="/suppliers" element={<SupplierManagement />} />
          <Route path="/contracts" element={<VendorContractManagement />} />
          <Route path="/procurement" element={<Procurement />} />
          <Route path="/receiving" element={<GoodsReceivingInspection />} />
          <Route path="/catalog" element={<CatalogStockManagement />} />
          <Route path="/monitoring" element={<InventoryMonitoring />} />
          <Route path="/forecasting" element={<DemandForecasting />} />
          <Route path="/storage" element={<WarehouseStorage />} />
          <Route path="/picking" element={<OrderPickingPackaging />} />
          <Route path="/quality" element={<QualityControl />} />
          <Route path="/shipments" element={<ShipmentScheduling />} />
          <Route path="/tracking" element={<TrackingUpdates />} />
          <Route path="/exceptions" element={<DeliveryExceptions />} />
          <Route path="/returns" element={<ReturnsProcessing />} />
          <Route path="/reconciliation" element={<ReconciliationReporting />} />
          <Route path="*" element={<div>404 - Not Found</div>} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
