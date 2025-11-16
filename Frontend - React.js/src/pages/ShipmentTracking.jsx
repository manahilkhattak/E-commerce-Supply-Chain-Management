import React from "react";
export default function ShipmentTracking(){
  return (
    <>
      <h2 className="section-title">Shipment Tracking (Mock)</h2>
      <div className="card">
        <p>Integrate with your shipment APIs here (Process 11–12). For now, this page is a UI placeholder that satisfies Person-4’s “6+ pages” and can be wired later.</p>
        <ul>
          <li>Search by tracking number</li>
          <li>Status timeline (Created → Shipped → In-Transit → Delivered)</li>
        </ul>
      </div>
    </>
  );
}
