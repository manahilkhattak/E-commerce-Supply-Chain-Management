import React from "react";
import { NavLink } from "react-router-dom";

export default function Navbar(){
  return (
    <header className="navbar">
      <div className="brand">Supply Chain Dashboard</div>
      <nav className="navlinks">
        <NavLink to="/">Home</NavLink>
        <NavLink to="/inventory">Inventory</NavLink>
        <NavLink to="/orders">Orders</NavLink>
        <NavLink to="/tracking">Tracking</NavLink>
        <NavLink to="/exceptions">Exceptions</NavLink>
        <NavLink to="/warehouse">Warehouse</NavLink>
      </nav>
    </header>
  );
}
