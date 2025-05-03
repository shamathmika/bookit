"use client";
import React, { useState, useEffect } from "react";
import { useAuth } from "@/context/AuthContext";
import TableCard from "../components/TableCard";
import { useRouter } from "next/navigation";

const ManageTablesPage = () => {
  const { user, isLoggedIn } = useAuth();
  const router = useRouter();
  const [tables, setTables] = useState([]);
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedRestaurantId, setSelectedRestaurantId] = useState(null);

  // Fetch restaurants when component mounts
  useEffect(() => {
    if (!isLoggedIn || !user) {
      router.push('/login');
      return;
    }

    const fetchRestaurants = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          setError('No authentication token found. Please log in again.');
          return;
        }

        const response = await fetch(`http://localhost:8080/api/manager/restaurants/restaurants-by-manager/${user.id}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          if (response.status === 401) {
            setError('Please log in again to view your restaurants');
            return;
          }
          if (response.status === 403) {
            setError('Access denied. You do not have permission to view this page.');
            return;
          }
          throw new Error('Failed to fetch restaurants');
        }

        const data = await response.json();
        setRestaurants(data);
        if (data.length > 0) {
          setSelectedRestaurantId(data[0].id);
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

  // Fetch tables when restaurant is selected
  useEffect(() => {
    if (!selectedRestaurantId) return;

    const fetchTables = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          setError('No authentication token found. Please log in again.');
          return;
        }

        const response = await fetch(`http://localhost:8080/api/manager/tables/${selectedRestaurantId}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          if (response.status === 401) {
            setError('Please log in again to view tables');
            return;
          }
          if (response.status === 403) {
            setError('Access denied. You do not have permission to view this page.');
            return;
          }
          throw new Error('Failed to fetch tables');
        }

        const data = await response.json();
        setTables(data);
      } catch (err) {
        console.error('Error fetching tables:', err);
        setError('Failed to load tables. Please try again later.');
      }
    };

    fetchTables();
  }, [selectedRestaurantId]);

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
      <h1 className="text-3xl font-bold mb-8">Manage Tables</h1>
      
      <div className="mb-8">
        <label className="block text-sm font-medium mb-1">Select Restaurant</label>
        <select
          className="border rounded px-3 py-2 w-full max-w-md"
          value={selectedRestaurantId || ""}
          onChange={(e) => setSelectedRestaurantId(e.target.value)}
        >
          {restaurants.map((restaurant) => (
            <option key={restaurant.id} value={restaurant.id}>
              {restaurant.name}
            </option>
          ))}
        </select>
      </div>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {tables.length === 0 ? (
          <div className="text-slate-400 col-span-full">No tables found.</div>
        ) : (
          tables.map((table) => (
            <TableCard
              key={table.id}
              table={table}
            />
          ))
        )}
      </div>
    </div>
  );
};

export default ManageTablesPage; 