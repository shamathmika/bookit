"use client";
import React, { useState } from "react";
import { useRestaurantContext } from "../context/RestaurantContext";
import TableCard from "../components/TableCard";

const ManageTablesPage = () => {
  const {
    restaurants,
    selectedRestaurantId,
    setSelectedRestaurant,
    toggleTableOccupied,
  } = useRestaurantContext();
  const [timeslot, setTimeslot] = useState({});

  const selected =
    restaurants.find((r) => r.id === selectedRestaurantId) || restaurants[0];

  React.useEffect(() => {
    if (restaurants.length && !selectedRestaurantId) {
      setSelectedRestaurant(restaurants[0].id);
    }
  }, [restaurants, selectedRestaurantId, setSelectedRestaurant]);

  if (!restaurants.length) {
    return <div className="text-slate-500">No restaurants found. Add one first.</div>;
  }

  const availableTables = selected?.tables.filter((t) => !t.occupied) || [];
  const occupiedTables = selected?.tables.filter((t) => t.occupied) || [];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Manage Tables</h1>
      <div className="mb-8">
        <label className="block text-sm font-medium mb-1">Select Restaurant</label>
        <select
          className="border rounded px-3 py-2"
          value={selected?.id || ""}
          onChange={(e) => setSelectedRestaurant(e.target.value)}
        >
          {restaurants.map((r) => (
            <option key={r.id} value={r.id}>{r.name}</option>
          ))}
        </select>
      </div>
      <div className="mb-10">
        <h2 className="text-xl font-semibold mb-4">Available Tables</h2>
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
          {availableTables.length === 0 ? (
            <div className="text-slate-400 col-span-full">No available tables.</div>
          ) : (
            availableTables.map((table) => (
              <TableCard
                key={table.id}
                table={table}
                isOccupiedSection={false}
                onToggle={() => toggleTableOccupied(selected.id, table.id)}
              />
            ))
          )}
        </div>
      </div>
      <div>
        <h2 className="text-xl font-semibold mb-4">Occupied Tables</h2>
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
          {occupiedTables.length === 0 ? (
            <div className="text-slate-400 col-span-full">No occupied tables.</div>
          ) : (
            occupiedTables.map((table) => (
              <TableCard
                key={table.id}
                table={table}
                isOccupiedSection={true}
                onToggle={() => toggleTableOccupied(selected.id, table.id)}
              />
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default ManageTablesPage; 