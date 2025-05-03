"use client";
import React, { useState, useEffect } from "react";
import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";
import { getRestaurantsByManager, addTablesToRestaurant } from "@/constants/apis";

const AddTablesPage = () => {
  const { user, isLoggedIn } = useAuth();
  const router = useRouter();
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [form, setForm] = useState({
    restaurantId: "",
    numberOfTables: 1,
    tableSize: 4,
  });

  // Fetch restaurants on mount
  useEffect(() => {
    if (!isLoggedIn || !user) {
      router.push('/login');
      return;
    }

    const fetchRestaurants = async () => {
      try {
        const data = await getRestaurantsByManager(user.id);
        setRestaurants(data);
        if (data.length > 0) {
          setForm(f => ({ ...f, restaurantId: data[0].id }));
        }
      } catch (err) {
        console.error('Error fetching restaurants:', err);
        setError('Failed to load restaurants. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, [user?.id, isLoggedIn, router]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.restaurantId) return;

    try {
      await addTablesToRestaurant(form.restaurantId, {
        numberOfTables: Number(form.numberOfTables),
        tableSize: Number(form.tableSize)
      });

      setSuccess('Tables added successfully!');
      setTimeout(() => {
        setSuccess(null);
        router.push('/manager/manage-tables');
      }, 2000);
    } catch (err) {
      console.error('Error adding tables:', err);
      setError('Failed to add tables. Please try again later.');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-slate-500">Loading...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-red-500">{error}</div>
      </div>
    );
  }

  if (restaurants.length === 0) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-slate-500">No restaurants found. Add one first.</div>
      </div>
    );
  }

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
            name="numberOfTables"
            min={1}
            value={form.numberOfTables}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Table Size (Seats)</label>
          <select
            name="tableSize"
            value={form.tableSize}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          >
            {[2, 4, 6, 8].map((n) => (
              <option key={n} value={n}>{n} Seats</option>
            ))}
          </select>
        </div>
        {error && <div className="text-red-500 mb-4">{error}</div>}
        {success && <div className="text-green-500 mb-4">{success}</div>}
        <div className="flex gap-4 mt-8">
          <button type="button" onClick={() => router.back()} className="rounded-md px-6 py-2 border border-slate-300">Cancel</button>
          <button type="submit" className="bg-rose-700 hover:bg-rose-800 rounded-md px-6 py-2 text-white">Add Tables</button>
        </div>
      </form>
    </div>
  );
};

export default AddTablesPage; 