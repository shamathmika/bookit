"use client";
import React, { useState } from "react";
import { useRestaurantContext } from "../context/RestaurantContext";
import { uuid } from "../utils/uuid";
import { useRouter } from "next/navigation";

const AddTablesPage = () => {
  const { restaurants, addTables } = useRestaurantContext();
  const router = useRouter();
  const [form, setForm] = useState({
    restaurantId: restaurants[0]?.id || "",
    count: 1,
    seats: 4,
    location: "Indoor",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.restaurantId) return;
    const rest = restaurants.find((r) => r.id === form.restaurantId);
    const nextNumber = rest ? rest.tables.length + 1 : 1;
    const tables = Array.from({ length: Number(form.count) }, (_, i) => ({
      id: uuid(),
      number: nextNumber + i,
      seats: Number(form.seats),
      timeslot: "11:00 AM - 1:00 PM",
      occupied: false,
      location: form.location,
    }));
    addTables(form.restaurantId, tables);
    setForm((f) => ({ ...f, count: 1 }));
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Add Tables</h1>
      <form className="bg-white rounded-xl p-8 max-w-xl" onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Select Restaurant</label>
          <select
            name="restaurantId"
            value={form.restaurantId}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          >
            {restaurants.map((r) => (
              <option key={r.id} value={r.id}>{r.name}</option>
            ))}
          </select>
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Number of Tables to Add</label>
          <input
            type="number"
            name="count"
            min={1}
            value={form.count}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Table Size (Seats)</label>
          <select
            name="seats"
            value={form.seats}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
          >
            {[2, 4, 6, 8].map((n) => (
              <option key={n} value={n}>{n} Seats</option>
            ))}
          </select>
        </div>
        <div className="mb-6">
          <label className="block text-sm font-medium mb-1">Table Location</label>
          <select
            name="location"
            value={form.location}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
          >
            <option value="Indoor">Indoor</option>
            <option value="Outdoor">Outdoor</option>
          </select>
        </div>
        <div className="flex gap-4 mt-8">
          <button type="button" onClick={() => router.back()} className="rounded-md px-6 py-2 border border-slate-300">Cancel</button>
          <button type="submit" className="bg-rose-700 hover:bg-rose-800 rounded-md px-6 py-2 text-white">Add Tables</button>
        </div>
      </form>
    </div>
  );
};

export default AddTablesPage; 