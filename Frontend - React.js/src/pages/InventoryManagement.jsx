import React, { useState } from "react";
import { getAvailableLocations, assignToShelf } from "../services/api";

export default function InventoryManagement(){
  const [params, setParams] = useState({ productType:"electronics", sizeCategory:"medium", temperatureControl:false });
  const [locations, setLocations] = useState([]);
  const [busy, setBusy] = useState(false);
  const [assignOut, setAssignOut] = useState(null);

  const search = async ()=> {
    setBusy(true);
    try {
      const {data} = await getAvailableLocations(params);
      setLocations(data);
    } finally { setBusy(false); }
  };

  const assign = async ()=> {
    setBusy(true);
    try{
      const {data} = await assignToShelf({
        productId:"PROD-12345",
        quantity:75,
        locationId: locations[0]?.locationId || "A-12-3",
        batchNumber:"BATCH-2025-03-01",
        expiryDate:"2026-03-01",
        placementStrategy:"FIFO",
        operatorId:"EMP-456",
        qualityCheckPassed:true,
        specialHandling:"fragile"
      });
      setAssignOut(data);
    } finally { setBusy(false); }
  };

  return (
    <>
      <h2 className="section-title">Inventory Management (Process 8)</h2>
      <div className="card">
        <div className="grid cols-3">
          <input className="input" value={params.productType} onChange={e=>setParams({...params,productType:e.target.value})} placeholder="productType" />
          <select className="select" value={params.sizeCategory} onChange={e=>setParams({...params,sizeCategory:e.target.value})}>
            <option>small</option><option>medium</option><option>large</option>
          </select>
          <select className="select" value={String(params.temperatureControl)} onChange={e=>setParams({...params,temperatureControl:e.target.value==="true"})}>
            <option value="false">No Temp Control</option>
            <option value="true">Temp Control</option>
          </select>
        </div>
        <div style={{marginTop:12}}>
          <button onClick={search} className="button" disabled={busy}>Find Locations</button>
          <button onClick={assign} className="button" disabled={busy || locations.length===0} style={{marginLeft:8}}>Assign to Shelf</button>
        </div>
      </div>

      <div className="card" style={{marginTop:16}}>
        <div className="section-title">Available Locations</div>
        {busy ? <div className="skeleton" style={{height:120,borderRadius:12}}/> :
        <table className="table">
          <thead><tr><th>ID</th><th>Type</th><th>Size</th><th>ProductType</th><th>Temp</th><th>Available</th></tr></thead>
          <tbody>
            {locations.map((l,i)=>(
              <tr key={i}>
                <td>{l.locationId}</td><td>{l.type}</td><td>{l.sizeCategory}</td>
                <td>{l.productType}</td><td>{String(l.temperatureControl)}</td>
                <td><span className="badge ok">{String(l.available)}</span></td>
              </tr>
            ))}
          </tbody>
        </table>}
      </div>

      {assignOut && (
        <div className="card" style={{marginTop:16}}>
          <div className="section-title">Shelf Assignment Result</div>
          <pre>{JSON.stringify(assignOut,null,2)}</pre>
        </div>
      )}
    </>
  );
}
