import React from "react";
export default function WarehouseOverview(){
  return (
    <>
      <h2 className="section-title">Warehouse Overview (Mock)</h2>
      <div className="grid cols-3">
        <div className="card"><div className="section-title">WH-01</div><div>Capacity: 82%</div></div>
        <div className="card"><div className="section-title">WH-02</div><div>Capacity: 65%</div></div>
        <div className="card"><div className="section-title">WH-03</div><div>Capacity: 44%</div></div>
      </div>
    </>
  );
}
