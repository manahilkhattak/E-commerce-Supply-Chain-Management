// src/services/api.js

// 🔧 Adjust to your backend URL
const BASE_URL = "http://localhost:8080/api";

// --------------------------------------------------
// Generic request helper
// --------------------------------------------------
async function request(path, options = {}) {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!res.ok) {
    let errorBody;
    try {
      errorBody = await res.json();
      console.error("API error body:", errorBody); // 👈 log full error
    } catch {
      throw new Error(`Request failed with status ${res.status}`);
    }

    const message =
      errorBody.message ||
      errorBody.debugMessage || // if present
      errorBody.error ||
      JSON.stringify(errorBody, null, 2);

    throw new Error(message);
  }

  if (res.status === 204) return null;
  return res.json();
}



/* ==================================================================== */
/*  CATALOG / PRODUCTS / STOCK                                          */
/*  ProductDTO, ProductResponseDTO, StockEntryDTO                       */
/* ==================================================================== */

export const CatalogApi = {
  createProduct: (body) =>
    request("/catalog/products", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  getProducts: () => request("/catalog/products"),

  getProductById: (id) => request(`/catalog/products/${id}`),

  updateProduct: (id, body) =>
    request(`/catalog/products/${id}`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),

  deleteProduct: (id) =>
    request(`/catalog/products/${id}`, {
      method: "DELETE",
    }),

  // ⚠️ Backend is POST /api/catalog/stock-update
  adjustStock: (body) =>
    request("/catalog/stock-update", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};

// Named exports used by pages
export const createProduct = CatalogApi.createProduct;
export const getProducts = CatalogApi.getProducts;
export const getProductById = CatalogApi.getProductById;
export const updateProduct = CatalogApi.updateProduct;
export const deleteProduct = CatalogApi.deleteProduct;
export const adjustStock = CatalogApi.adjustStock;

/* ==================================================================== */
/*  SUPPLIERS                                                           */
/*  SupplierRegistrationDTO, SupplierApprovalDTO                        */
/* ==================================================================== */

// Explicit helper used by SupplierManagement.jsx
export function registerSupplier(body) {
  return request("/suppliers/register", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

// Wrapper for status/approval update.
// Backend: PUT /api/suppliers/{supplierId}/approval
export function updateSupplierApprovalStatus(
  supplierId,
  statusOrBody,
  maybeNotes
) {
  let body;
  if (typeof statusOrBody === "object" && statusOrBody !== null) {
    body = statusOrBody;
  } else {
    body = { status: statusOrBody, notes: maybeNotes || "" };
  }

  return request(`/suppliers/${supplierId}/approval`, {
    method: "PUT",
    body: JSON.stringify(body),
  });
}

export const SupplierApi = {
  register: registerSupplier,
  list: () => request("/suppliers"),
  getById: (id) => request(`/suppliers/${id}`),
  updateStatus: (id, body) =>
    request(`/suppliers/${id}/approval`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),
};
/* ==================================================================== */
/*  CONTRACTS + SLA                                                     */
/*  ContractDTO, ContractResponseDTO, SLACreationDTO, SLAResponseDTO    */
/* ==================================================================== */
/* ==================================================================== */
/*  CONTRACTS + SLA                                                     */
/*  ContractDTO, ContractResponseDTO, SLACreationDTO, SLAResponseDTO    */
/* ==================================================================== */

export const ContractApi = {
  // Create a new contract
  createContract: (body) =>
    request("/contracts", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // List all contracts
  getContracts: () => request("/contracts"),

  // Get contract by ID
  getContractById: (id) => request(`/contracts/${id}`),

  // Update contract
  updateContract: (id, body) =>
    request(`/contracts/${id}`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),

  // Delete contract
  deleteContract: (id) =>
    request(`/contracts/${id}`, {
      method: "DELETE",
    }),

  /**
   * Add SLA to a contract
   * Backend: POST /api/contracts/sla
   * Body: SLACreationDTO (contains contractId + SLA fields)
   */
  addSlaToContract: (body) =>
    request("/contracts/sla", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  /**
   * Get SLAs for a contract
   * GET /api/contracts/{contractId}/slas
   */
  getSlasForContract: (contractId) =>
    request(`/contracts/${contractId}/slas`),
};

// Named exports
export const createContract = ContractApi.createContract;
export const getContracts = ContractApi.getContracts;
export const getContractById = ContractApi.getContractById;
export const updateContract = ContractApi.updateContract;
export const deleteContract = ContractApi.deleteContract;
export const addSlaToContract = ContractApi.addSlaToContract;




/* ==================================================================== */
/*  FORECASTING & REORDER PLANNING                                      */
/*  ForecastDTO, ForecastResponseDTO, ReorderPlanDTO, ConvertToPODTO    */
/* ==================================================================== */

export const ForecastApi = {
  // Backend: POST /api/forecasting/demand-forecasts
  createDemandForecast: (body) =>
    request("/forecasting/demand-forecasts", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // Backend: GET /api/forecasting/demand-forecasts
  getDemandForecasts: () => request("/forecasting/demand-forecasts"),

  getForecastById: (id) =>
    request(`/forecasting/demand-forecasts/${id}`),

  // Backend: POST /api/forecasting/reorder-plans
  createReorderPlan: (body) =>
    request("/forecasting/reorder-plans", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // Backend: GET /api/forecasting/reorder-plans
  getReorderPlans: () => request("/forecasting/reorder-plans"),

  getReorderPlanById: (id) =>
    request(`/forecasting/reorder-plans/${id}`),

  // Backend: GET /api/forecasting/reorder-plans/critical (not used in UI yet)
  getCriticalReorderPlans: () =>
    request("/forecasting/reorder-plans/critical"),
};

// Convenience exports used by pages
// DemandForecasting.jsx calls getDemandForecast(productId, horizonDays)
// (for now we just ignore the filters and return all forecasts)
export const getDemandForecast = (productId, horizonDays) =>
  ForecastApi.getDemandForecasts();

// High-level helper used by DemandForecasting.jsx for the
// "Generate Reorder Plan" button
export const generateReorderPlan = (body) =>
  ForecastApi.createReorderPlan(body);

export const createForecast = ForecastApi.createDemandForecast;
export const getForecasts = ForecastApi.getDemandForecasts;
export const createReorderPlan = ForecastApi.createReorderPlan;
export const getReorderPlans = ForecastApi.getReorderPlans;



/* ==================================================================== */
/*  INVENTORY + MONITORING + ALERTS                                     */
/*  InventoryDTO, InventoryResponseDTO, InventoryMonitoringDTO,         */
/*  StockAlertDTO, StockUpdateDTO                                       */
/* ==================================================================== */

export const InventoryApi = {
  // Inventory monitoring: POST /api/inventory/monitoring
  addProductToMonitoring: (body) =>
    request("/inventory/monitoring", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // Get all monitored items: GET /api/inventory/monitoring
  getMonitoringItems: () => request("/inventory/monitoring"),

  // Get monitoring info by product: GET /api/inventory/monitoring/product/{productId}
  getMonitoringByProductId: (productId) =>
    request(`/inventory/monitoring/product/${productId}`),

  // Update stock from events: POST /api/inventory/stock-update
  updateInventoryStock: (body) =>
    request("/inventory/stock-update", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // Optional: legacy inventory endpoint from your earlier design
  upsertInventory: (body) =>
    request("/inventory", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // Alerts & health
  // GET /api/inventory/alerts/active
  getActiveInventoryAlerts: () =>
    request("/inventory/alerts/active"),

  // PUT /api/inventory/alerts/resolve
  resolveInventoryAlert: (body) =>
    request("/inventory/alerts/resolve", {
      method: "PUT",
      body: JSON.stringify(body),
    }),

  // POST /api/inventory/health-check
  runInventoryHealthCheck: () =>
    request("/inventory/health-check", {
      method: "POST",
    }),

  // Extra helpers if you want to use low/out/overstock lists
  getLowStockItems: () => request("/inventory/low-stock"),
  getOutOfStockItems: () => request("/inventory/out-of-stock"),
  getOverstockItems: () => request("/inventory/overstock"),
};

// Named exports matching InventoryMonitoring.jsx
// 🔹 Inventory & Monitoring APIs

export const addProductToInventoryMonitoring = (body) =>
  request("/inventory/monitoring", {
    method: "POST",
    body: JSON.stringify(body),
  });

export const updateInventoryStock = (body) =>
  request("/inventory/stock-update", {
    method: "POST",
    body: JSON.stringify(body),
  });

export const runInventoryHealthCheck = () =>
  request("/inventory/health", {
    method: "GET",
  });

export const getActiveInventoryAlerts = () =>
  request("/inventory/alerts/active", {
    method: "GET",
  });

export const resolveInventoryAlert = (alertId, resolutionComment) =>
  request("/inventory/alerts/resolve", {
    method: "PUT",
    body: JSON.stringify({
      alertId,              // must be numeric on the caller side
      resolutionComment,    // text describing how it was resolved
    }),
  });


/* ==================================================================== */
/*  ORDERS                                                              */
/*  OrderDTO, OrderResponseDTO, OrderStatusUpdateDTO                    */
/* ==================================================================== */

export const OrderApi = {
  createOrder: (body) =>
    request("/orders", {
      method: "POST",
      body: JSON.stringify(body),
    }),
  getOrders: () => request("/orders"),
  getOrderById: (id) => request(`/orders/${id}`),
  updateOrderStatus: (id, body) =>
    request(`/orders/${id}/status`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),
};

export const createOrder = OrderApi.createOrder;
export const getOrders = OrderApi.getOrders;
export const getOrderById = OrderApi.getOrderById;
export const updateOrderStatus = OrderApi.updateOrderStatus;

/* ==================================================================== */
/*  PICKING & PACKAGING                                                 */
/*  PickListDTO, PickListResponseDTO, PackageDTO, PackageResponseDTO    */
/* ==================================================================== */

export const PickingApi = {
  // POST /api/picking/pick-lists
  createPickList: (body) =>
    request("/picking/pick-lists", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/picking/pick-lists
  getPickLists: () => request("/picking/pick-lists"),

  getPickListById: (id) =>
    request(`/picking/pick-lists/${id}`),

  // PUT /api/picking/pick-items/update
  updatePickItem: (body) =>
    request("/picking/pick-items/update", {
      method: "PUT",
      body: JSON.stringify(body),
    }),

  // POST /api/picking/packages
  createPackage: (body) =>
    request("/picking/packages", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/picking/packages
  getPackages: () => request("/picking/packages"),

  getPackageById: (id) =>
    request(`/picking/packages/${id}`),

  // PUT /api/picking/packages/{packageId}/mark-packed
  markPackageAsPacked: (packageId, packedBy) =>
    request(`/picking/packages/${packageId}/mark-packed?packedBy=${encodeURIComponent(
      packedBy
    )}`, {
      method: "PUT",
    }),
};

// Named exports used in OrderPickingPackaging.jsx & OrderProcessing.jsx
export const createPickList = PickingApi.createPickList;
export const getPickLists = PickingApi.getPickLists;
export const updatePickItem = PickingApi.updatePickItem;
export const createPackage = PickingApi.createPackage;
export const getPackages = PickingApi.getPackages;

// High-level helpers to match page imports
export const generatePickingList = (body) =>
  PickingApi.createPickList(body);

export const updatePickingStatus = (body) =>
  PickingApi.updatePickItem(body);

export const createPackaging = (body) =>
  PickingApi.createPackage(body);

// completePackaging(body) → we map to markPackageAsPacked using packageId + sealedBy
export const completePackaging = (body) =>
  PickingApi.markPackageAsPacked(body.packageId, body.sealedBy);

/* ==================================================================== */
/*  PROCUREMENT (PURCHASE ORDERS)                                       */
/*  PurchaseOrderDTO, PurchaseOrderResponseDTO                          */
/* ==================================================================== */

/* ==================================================================== */
/*  PROCUREMENT (PURCHASE ORDERS)                                       */
/*  PurchaseOrderDTO, PurchaseOrderResponseDTO                          */
/* ==================================================================== */

export const ProcurementApi = {
  // Create a purchase order (POST /api/procurement/purchase-orders)
  createPurchaseOrder: (body) =>
    request("/procurement/purchase-orders", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // List POs
  getPurchaseOrders: () => request("/procurement/purchase-orders"),

  // Get single PO by numeric ID (poId)
  getPurchaseOrderById: (id) =>
    request(`/procurement/purchase-orders/${id}`),

  // Approve PO (PUT /api/procurement/purchase-orders/{poId}/approve?approvedBy=...)
  approvePurchaseOrder: (poId, approvedBy) =>
    request(
      `/procurement/purchase-orders/${poId}/approve?approvedBy=${encodeURIComponent(
        approvedBy
      )}`,
      {
        method: "PUT",
      }
    ),
};

// named exports
export const createPurchaseOrder = ProcurementApi.createPurchaseOrder;
export const getPurchaseOrders = ProcurementApi.getPurchaseOrders;
export const getPurchaseOrderById = ProcurementApi.getPurchaseOrderById;
export const approvePurchaseOrder = ProcurementApi.approvePurchaseOrder;


/* ==================================================================== */
/*  RECEIVING & INSPECTION                                              */
/*  GoodsReceiptDTO, GoodsReceiptResponseDTO, InspectionDTO             */
/* ==================================================================== */

/* ==================================================================== */
/*  RECEIVING & INSPECTION                                              */
/*  GoodsReceiptDTO, GoodsReceiptResponseDTO, InspectionDTO             */
/* ==================================================================== */

export const ReceivingApi = {
  // Create a goods receipt (GoodsReceiptDTO)
  createGoodsReceipt: (body) =>
    request("/receiving/goods-receipts", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // List all receipts
  getGoodsReceipts: () => request("/receiving/goods-receipts"),

  // Get single receipt by ID
  getGoodsReceiptById: (receiptId) =>
    request(`/receiving/goods-receipts/${receiptId}`),

  /**
   * Create / submit an inspection (InspectionDTO)
   */
  createQualityInspection: (body) =>
    request("/receiving/inspections", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // Alias – use this in the page if you want
  performInspection: (body) =>
    request("/receiving/inspections", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};

// Named exports (so you can import them directly)
export const createGoodsReceipt = ReceivingApi.createGoodsReceipt;
export const getGoodsReceipts = ReceivingApi.getGoodsReceipts;
export const getGoodsReceiptById = ReceivingApi.getGoodsReceiptById;
export const createQualityInspection = ReceivingApi.createQualityInspection;
export const performInspection = ReceivingApi.performInspection;


/* ==================================================================== */
/*  QUALITY CONTROL                                                     */
/*  QualityCheckDTO, QualityResultDTO, RecheckRequestDTO                */
/* ==================================================================== */

export const QualityApi = {
  // POST /api/quality/checks
  createQualityCheck: (body) =>
    request("/quality/checks", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/quality/checks
  getQualityChecks: () => request("/quality/checks"),

  getQualityCheckById: (id) =>
    request(`/quality/checks/${id}`),

  // PUT /api/quality/checks/results
  submitQualityResults: (body) =>
    request("/quality/checks/results", {
      method: "PUT",
      body: JSON.stringify(body),
    }),

  // PUT /api/quality/checks/recheck
  requestQualityRecheck: (body) =>
    request("/quality/checks/recheck", {
      method: "PUT",
      body: JSON.stringify(body),
    }),
};

export const createQualityCheck = QualityApi.createQualityCheck;
export const getQualityChecks = QualityApi.getQualityChecks;
export const getQualityCheckById = QualityApi.getQualityCheckById;
export const submitQualityResult = QualityApi.submitQualityResults;
export const requestQualityRecheck =
  QualityApi.requestQualityRecheck;

// Alias for page import (QualityControl.jsx)
export const submitQualityCheckResults = QualityApi.submitQualityResults;

/* ==================================================================== */
/*  RECONCILIATION & REPORTING                                         */
/*  ReconciliationDTO, ReportDTO                                       */
/* ==================================================================== */

export const ReconciliationApi = {
  // POST /api/reconciliation/reports
  createReconciliationReport: (body) =>
    request("/reconciliation/reports", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/reconciliation/reports
  getReconciliationReports: () =>
    request("/reconciliation/reports"),

  getReconciliationReportById: (id) =>
    request(`/reconciliation/reports/${id}`),

  // Backend: PUT /api/reconciliation/reports/{reportId}/complete
  finalizeReconciliationReport: (reportId, body) =>
    request(`/reconciliation/reports/${reportId}/complete`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),
};

export const createReconciliationReport =
  ReconciliationApi.createReconciliationReport;
export const getReconciliationReports =
  ReconciliationApi.getReconciliationReports;
export const finalizeReconciliationReport =
  ReconciliationApi.finalizeReconciliationReport;

// ReconciliationReporting.jsx expects generateInventoryReport()
// Map it to createReconciliationReport for now.
export const generateInventoryReport =
  ReconciliationApi.createReconciliationReport;

/* ==================================================================== */
/*  RETURNS & RESTOCKING                                               */
/*  ReturnOrderDTO, RestockDTO                                         */
/* ==================================================================== */

export const ReturnsApi = {
  // POST /api/returns
  createReturnOrder: (body) =>
    request("/returns", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/returns
  getReturnOrders: () => request("/returns"),

  // GET /api/returns/{returnOrderId}
  getReturnOrderById: (id) => request(`/returns/${id}`),

  // POST /api/returns/restock
  createRestock: (body) =>
    request("/returns/restock", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/returns/restock
  getRestocks: () => request("/returns/restock"),
};

export const createReturnOrder = ReturnsApi.createReturnOrder;
export const getReturnOrders = ReturnsApi.getReturnOrders;
export const createRestock = ReturnsApi.createRestock;

// ReturnsProcessing.jsx also wants approveReturnOrder – backend uses a status/approval endpoint
// but it’s not strictly defined as /approve; for now we can emulate via restock or leave a stub.
// Here we just alias to getReturnOrderById to avoid breaking imports.
// You can later wire it to the correct approval endpoint if you add one.
export const approveReturnOrder = ReturnsApi.getReturnOrderById;

/* ==================================================================== */
/*  SHIPMENTS & DISPATCH                                               */
/*  ShipmentDTO, ShipmentUpdateDTO, DispatchDTO                         */
/* ==================================================================== */

export const ShipmentApi = {
  // POST /api/shipments
  createShipment: (body) =>
    request("/shipments", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/shipments
  getShipments: () => request("/shipments"),

  // GET /api/shipments/{shipmentId}
  getShipmentById: (id) => request(`/shipments/${id}`),

  // PUT /api/shipments/{shipmentId}/status
  updateShipmentStatus: (shipmentId, body) =>
    request(`/shipments/${shipmentId}/status`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),

  // POST /api/shipments/dispatch
  scheduleDispatch: (body) =>
    request("/shipments/dispatch", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};

export const createShipment = ShipmentApi.createShipment;
export const getShipments = ShipmentApi.getShipments;
export const getShipmentById = ShipmentApi.getShipmentById;
export const updateShipmentStatus = ShipmentApi.updateShipmentStatus;
export const scheduleDispatch = ShipmentApi.scheduleDispatch;

// Alias for ShipmentScheduling.jsx
export const scheduleShipmentDispatch = ShipmentApi.scheduleDispatch;

/* ==================================================================== */
/*  TRACKING & DELIVERY STATUS                                         */
/*  TrackingDTO, DeliveryStatusResponseDTO                              */
/* ==================================================================== */

export const TrackingApi = {
  // POST /api/tracking/events
  createTrackingEvent: (body) =>
    request("/tracking/events", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // GET /api/tracking/history/{trackingNumber}
  getTrackingEventsByNumber: (trackingNumber) =>
    request(`/tracking/history/${encodeURIComponent(trackingNumber)}`),

  // GET /api/tracking/status/{trackingNumber}
  getDeliveryStatus: (trackingNumber) =>
    request(`/tracking/status/${encodeURIComponent(trackingNumber)}`),
};

// Original exports
export const createTrackingEvent = TrackingApi.createTrackingEvent;
export const getTrackingEventsByNumber =
  TrackingApi.getTrackingEventsByNumber;
export const getDeliveryStatus = TrackingApi.getDeliveryStatus;

// Aliases expected by TrackingUpdates.jsx
export const addTrackingEvent = TrackingApi.createTrackingEvent;
export const getTrackingStatus = TrackingApi.getDeliveryStatus;

/* ==================================================================== */
/*  WAREHOUSE / STORAGE / SHELVES                                      */
/*  WarehouseDTO, ShelfPlacementDTO                                    */
/* ==================================================================== */

export const WarehouseApi = {
  // Warehouses CRUD
  createWarehouse: (body) =>
    request("/warehouse/warehouses", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  getWarehouses: () => request("/warehouse/warehouses"),

  getWarehouseById: (id) =>
    request(`/warehouse/warehouses/${id}`),

  // Shelf locations by warehouse
  getShelfLocations: (warehouseId) =>
    request(`/warehouse/warehouses/${warehouseId}/shelves`),

  // Get shelves with capacity:
  // GET /api/warehouse/shelves/available?minUnits=&minVolume=&temperatureControl=
  getAvailableShelves: (params = {}) => {
    const search = new URLSearchParams();
    if (params.minUnits != null)
      search.set("minUnits", params.minUnits);
    if (params.minVolume != null)
      search.set("minVolume", params.minVolume);
    if (params.temperatureControl != null)
      search.set("temperatureControl", params.temperatureControl);

    const qs = search.toString();
    return request(
      `/warehouse/shelves/available${qs ? `?${qs}` : ""}`
    );
  },

  // Shelf placement: POST /api/warehouse/shelf-placement
  placeOnShelf: (body) =>
    request("/warehouse/shelf-placement", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};

export const createWarehouse = WarehouseApi.createWarehouse;
export const getWarehouses = WarehouseApi.getWarehouses;
export const getWarehouseById = WarehouseApi.getWarehouseById;
export const getShelfLocations = WarehouseApi.getShelfLocations;
export const placeOnShelf = WarehouseApi.placeOnShelf;

// Aliases expected by InventoryManagement.jsx & WarehouseStorage.jsx

// InventoryManagement.jsx wants getAvailableLocations(params)
export const getAvailableLocations = (params) =>
  WarehouseApi.getAvailableShelves({
    temperatureControl: params?.temperatureControl,
  });

// WarehouseStorage.jsx wants getAvailableStorageLocations(...)
export const getAvailableStorageLocations = (params) =>
  WarehouseApi.getAvailableShelves({
    temperatureControl: params?.tempControl ?? params?.temperatureControl,
  });

// Assign helpers
export const assignToShelf = (body) =>
  WarehouseApi.placeOnShelf(body);

export const assignProductToShelf = (body) =>
  WarehouseApi.placeOnShelf(body);

/* ==================================================================== */
/*  DELIVERY EXCEPTIONS                                                 */
/*  DeliveryExceptionDTO, ResolutionDTO                                 */
/* ==================================================================== */

export const DeliveryExceptionApi = {
  // POST /api/exceptions
  createDeliveryException: (body) =>
    request("/exceptions", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  // POST /api/exceptions/resolve
  resolveDeliveryException: (body) =>
    request("/exceptions/resolve", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};

export const createDeliveryException =
  DeliveryExceptionApi.createDeliveryException;
export const resolveDeliveryException =
  DeliveryExceptionApi.resolveDeliveryException;
