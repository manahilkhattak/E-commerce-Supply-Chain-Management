import React, { useState } from "react";
import { generatePickingList, updatePickingStatus, createPackaging, completePackaging } from "../services/api";

export default function OrderProcessing(){
  const [pickingOut, setPickingOut] = useState(null);
  const [statusOut, setStatusOut] = useState(null);
  const [packOut, setPackOut] = useState(null);
  const [completeOut, setCompleteOut] = useState(null);
  const [busy, setBusy] = useState(false);

  const genPick = async ()=>{
    setBusy(true);
    try{
      const {data} = await generatePickingList({
        orderId:"ORD-789456",
        priority:"STANDARD",
        items:[{productId:"PROD-12345",quantity:2,sku:"SKU-WH-12345"}],
        pickingStrategy:"SHORTEST_PATH",
        maxPickingTime:30
      });
      setPickingOut(data);
    } finally { setBusy(false); }
  };

  const updStatus = async ()=>{
    setBusy(true);
    try{
      const {data} = await updatePickingStatus({
        pickingListId: pickingOut?.pickingListId || "PICK-LOCAL",
        itemsPicked:[{productId:"PROD-12345",quantityPicked:2,locationId:"A-12-3",pickedAt:new Date().toISOString(),verificationCode:"VER-12345"}],
        status:"IN_PROGRESS", pickerId:"PICKER-101", currentLocation:"A-12", issues:[]
      });
      setStatusOut(data);
    } finally { setBusy(false); }
  };

  const makePack = async ()=>{
    setBusy(true);
    try{
      const {data} = await createPackaging({
        pickingListId: pickingOut?.pickingListId || "PICK-LOCAL",
        orderId:"ORD-789456",
        packageType:"STANDARD_BOX",
        fragileItems:true, giftWrap:false, customMessage:null,
        sustainablePackaging:true, insuranceRequired:false, declaredValue:150
      });
      setPackOut(data);
    } finally { setBusy(false); }
  };

  const finishPack = async ()=>{
    setBusy(true);
    try{
      const {data} = await completePackaging({
        packageId: packOut?.packageId || "PKG-LOCAL",
        actualWeight:1.45,
        dimensions:{length:30,width:20,height:15,unit:"cm"},
        sealedBy:"PACKER-202",
        qualityCheckPassed:true,
        photosUrls:[],
        packingMaterialsUsed:{bubbleWrap:2,recyclableFill:100}
      });
      setCompleteOut(data);
    } finally { setBusy(false); }
  };

  return (
    <>
      <h2 className="section-title">Order Processing (Process 9)</h2>
      <div className="card">
        <button className="button" onClick={genPick} disabled={busy}>Generate Picking List</button>
        <button className="button" onClick={updStatus} disabled={busy || !pickingOut} style={{marginLeft:8}}>Update Picking Status</button>
        <button className="button" onClick={makePack} disabled={busy || !pickingOut} style={{marginLeft:8}}>Create Packaging</button>
        <button className="button" onClick={finishPack} disabled={busy || !packOut} style={{marginLeft:8}}>Complete Packaging</button>
      </div>

      {pickingOut && <div className="card" style={{marginTop:16}}><div className="section-title">Picking</div><pre>{JSON.stringify(pickingOut,null,2)}</pre></div>}
      {statusOut &&  <div className="card" style={{marginTop:16}}><div className="section-title">Status</div><pre>{JSON.stringify(statusOut,null,2)}</pre></div>}
      {packOut &&    <div className="card" style={{marginTop:16}}><div className="section-title">Packaging</div><pre>{JSON.stringify(packOut,null,2)}</pre></div>}
      {completeOut &&<div className="card" style={{marginTop:16}}><div className="section-title">Complete</div><pre>{JSON.stringify(completeOut,null,2)}</pre></div>}
    </>
  );
}
