"use client";
import React, { useState, useEffect } from "react";
import { useAuth } from "@/context/AuthContext";
import TableCard from "../components/TableCard";

const ManageTablesPage = () => {
  const { user } = useAuth();
  const [restaurants, setRestaurants] = useState([]);
  const [selectedRestaurantId, setSelectedRestaurantId] = useState("");
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch restaurants
  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await fetch(
          `http://localhost:8080/api/manager/restaurants/restaurants-by-manager/${user.id}`,
          {
            headers: {
              "Authorization": `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("Failed to fetch restaurants");
        }

        const data = await response.json();
        setRestaurants(data);
        if (data.length > 0) {
          setSelectedRestaurantId(data[0].id);
        }
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (user?.id) {
      fetchRestaurants();
    }
  }, [user?.id]);

  // Fetch tables when restaurant is selected
  useEffect(() => {
    const fetchTables = async () => {
      if (!selectedRestaurantId) return;

      try {
        const token = localStorage.getItem("token");
        const response = await fetch(
          `http://localhost:8080/api/manager/tables/${selectedRestaurantId}`,
          {
            headers: {
              "Authorization": `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("Failed to fetch tables");
        }

        const data = await response.json();
        setTables(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchTables();
  }, [selectedRestaurantId]);

  const handleTableStatusChange = async (tableId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(
        `http://localhost:8080/api/manager/tables/${selectedRestaurantId}/toggle-status/${tableId}`,
        {
          method: "PUT",
          headers: {
            "Authorization": `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to update table status");
      }

      // Refresh tables after status change
      const updatedTables = await fetch(
        `http://localhost:8080/api/manager/tables/${selectedRestaurantId}`,
        {
          headers: {
            "Authorization": `Bearer ${token}`,
          },
        }
      ).then(res => res.json());

      setTables(updatedTables);
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) {
    return <div className="text-slate-500">Loading restaurants...</div>;
  }

  if (error) {
    return <div className="text-red-500">Error: {error}</div>;
  }

  if (!restaurants.length) {
    return <div className="text-slate-500">No restaurants found. Add one first.</div>;
  }

  const availableTables = tables.filter((t) => t.status === "AVAILABLE");
  const occupiedTables = tables.filter((t) => t.status === "OCCUPIED");

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Manage Tables</h1>
      <div className="mb-8">
        <label className="block text-sm font-medium mb-1">Select Restaurant</label>
        <select
          className="border rounded px-3 py-2"
          value={selectedRestaurantId}
          onChange={(e) => setSelectedRestaurantId(e.target.value)}
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
                table={{
                  id: table.id,
                  number: table.tableNumber,
                  seats: table.capacity,
                  status: table.status
                }}
                isOccupiedSection={false}
                onToggle={() => handleTableStatusChange(table.id)}
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
                table={{
                  id: table.id,
                  number: table.tableNumber,
                  seats: table.capacity,
                  status: table.status
                }}
                isOccupiedSection={true}
                onToggle={() => handleTableStatusChange(table.id)}
              />
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default ManageTablesPage; 