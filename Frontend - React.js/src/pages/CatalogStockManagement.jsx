// src/pages/CatalogStockManagement.jsx
import React, { useState, useEffect } from "react";
import { createProduct, getProducts } from "../services/api";

export default function Catalog() {
  const [product, setProduct] = useState({
    productName: "",
    productSku: "",           // NOTE: camelCase 'Sku' exactly
    description: "",
    category: "",
    brand: "",
    supplierId: "",           // string in UI, converted to number before send
    sellingPrice: "",
    costPrice: "",
    currentStock: "0",        // required
    minimumStockLevel: "",    // required
    maximumStockLevel: "",
    reorderPoint: "",
    weight: "",
    dimensions: "",
    unitOfMeasurement: "PIECE",
    barcode: "",
    imageUrl: "",
    isActive: true,           // backend name is isActive
  });

  const [products, setProducts] = useState([]);

  // generic change handler
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setProduct((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const saveProduct = async () => {
  try {
    if (!product.productName.trim()) {
      alert("Product name is required");
      return;
    }
    if (!product.productSku.trim()) {
      alert("Product SKU is required");
      return;
    }
    if (!product.description.trim()) {
      alert("Description is required");
      return;
    }
    if (!product.category.trim()) {
      alert("Category is required");
      return;
    }
    if (!product.sellingPrice) {
      alert("Selling price is required");
      return;
    }
    if (product.minimumStockLevel === "") {
      alert("Minimum stock level is required");
      return;
    }
    if (product.currentStock === "") {
      alert("Current stock is required");
      return;
    }

    const payload = {
      productName: product.productName,
      productSku: product.productSku,
      description: product.description,
      category: product.category,
      brand: product.brand || null,
      supplierId: product.supplierId ? Number(product.supplierId) : null,
      sellingPrice: Number(product.sellingPrice),
      costPrice:
        product.costPrice === "" ? 0 : Number(product.costPrice),
      currentStock: Number(product.currentStock),
      minimumStockLevel: Number(product.minimumStockLevel),

      // 👇 IMPORTANT: never null, default to 0
      maximumStockLevel:
        product.maximumStockLevel === ""
          ? null
          : Number(product.maximumStockLevel),
      reorderPoint:
        product.reorderPoint === ""
          ? 0
          : Number(product.reorderPoint),

      weight: product.weight === "" ? null : Number(product.weight),
      dimensions: product.dimensions || null,
      unitOfMeasurement: product.unitOfMeasurement,
      barcode: product.barcode || null,
      imageUrl: product.imageUrl || null,
      isActive: product.isActive,
    };

    console.log("Create product payload:", payload); // 👈 debug

    const res = await createProduct(payload);
    console.log("Create product response:", res);
    alert("Product created successfully");
  } catch (err) {
    console.error("Create product error:", err);
    alert(err.message || "An unexpected error occurred");
  }
};


  const fetchProducts = async () => {
    try {
      const res = await getProducts();
      // depending on your backend, res may be an array or { data: [...] }
      setProducts(Array.isArray(res) ? res : res.data || []);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  return (
    <div className="page">
      <h2>Catalog & Stock Management</h2>

      <div className="card">
        <h3>Create Product</h3>

        <div className="grid">
          <input
            name="productName"
            placeholder="Name*"
            value={product.productName}
            onChange={handleChange}
          />
          <input
            name="productSku"
            placeholder="SKU*"
            value={product.productSku}
            onChange={handleChange}
          />
          <input
            name="description"
            placeholder="Description*"
            value={product.description}
            onChange={handleChange}
          />
          <input
            name="category"
            placeholder="Category*"
            value={product.category}
            onChange={handleChange}
          />

          <input
            name="brand"
            placeholder="Brand"
            value={product.brand}
            onChange={handleChange}
          />
          <input
            name="supplierId"
            placeholder="Supplier ID"
            value={product.supplierId}
            onChange={handleChange}
          />

          <input
            name="sellingPrice"
            placeholder="Selling Price*"
            value={product.sellingPrice}
            onChange={handleChange}
          />
          <input
            name="costPrice"
            placeholder="Cost Price"
            value={product.costPrice}
            onChange={handleChange}
          />

          <input
            name="currentStock"
            placeholder="Current Stock*"
            value={product.currentStock}
            onChange={handleChange}
          />
          <input
            name="minimumStockLevel"
            placeholder="Min Stock Level*"
            value={product.minimumStockLevel}
            onChange={handleChange}
          />

          <input
            name="maximumStockLevel"
            placeholder="Max Stock Level"
            value={product.maximumStockLevel}
            onChange={handleChange}
          />
          <input
            name="reorderPoint"
            placeholder="Reorder Point"
            value={product.reorderPoint}
            onChange={handleChange}
          />

          <input
            name="weight"
            placeholder="Weight (kg)"
            value={product.weight}
            onChange={handleChange}
          />
          <input
            name="dimensions"
            placeholder="Dimensions"
            value={product.dimensions}
            onChange={handleChange}
          />

          <select
            name="unitOfMeasurement"
            value={product.unitOfMeasurement}
            onChange={handleChange}
          >
            <option value="PIECE">PIECE</option>
            <option value="KG">KG</option>
            <option value="LITER">LITER</option>
            <option value="BOX">BOX</option>
            <option value="METER">METER</option>
            <option value="SET">SET</option>
          </select>

          <input
            name="barcode"
            placeholder="Barcode"
            value={product.barcode}
            onChange={handleChange}
          />

          <input
            name="imageUrl"
            placeholder="Image URL"
            value={product.imageUrl}
            onChange={handleChange}
          />
        </div>

        <label>
          <input
            type="checkbox"
            name="isActive"
            checked={product.isActive}
            onChange={handleChange}
          />
          Active
        </label>

        <button onClick={saveProduct}>Save Product</button>
      </div>

      <h3>Products</h3>
      <div className="product-list">
        {products.map((p) => (
          <div key={p.productId} className="card">
            <h4>{p.productName}</h4>
            <p>SKU: {p.productSku}</p>
            <p>Category: {p.category}</p>
            <p>Selling Price: {p.sellingPrice}</p>
            <p>Current Stock: {p.currentStock}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
