import React from "react";
export default function StatCard({label,value,sub}){
  return (
    <div className="card">
      <div style={{opacity:.75}}>{label}</div>
      <div style={{fontSize:28,fontWeight:700,marginTop:6}}>{value}</div>
      {sub && <div style={{opacity:.6,marginTop:4,fontSize:13}}>{sub}</div>}
    </div>
  );
}
