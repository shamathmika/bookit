"use client";
import React, { useState, useEffect } from "react";
import { useAuth } from "@/context/AuthContext";
import RestaurantCard from "../components/RestaurantCard";

const DashboardPage = () => {
  const { user } = useAuth();
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

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
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user?.id) {
      fetchRestaurants();
    }
  }, [user?.id]);

  const handleDelete = (deletedRestaurantId) => {
    setRestaurants(restaurants.filter(r => r.id !== deletedRestaurantId));
    setSuccessMessage("Restaurant deleted successfully!");
    setTimeout(() => setSuccessMessage(""), 3000);
  };

  if (loading) {
    return <div className="text-slate-500">Loading restaurants...</div>;
  }

  if (error) {
    return <div className="text-red-500">Error: {error}</div>;
  }

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">My Restaurants</h1>
      {successMessage && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
          {successMessage}
        </div>
      )}
      {restaurants.length === 0 ? (
        <div className="text-slate-500">No restaurants yet. Add one to get started!</div>
      ) : (
        <div className="grid gap-6 lg:grid-cols-3">
          {restaurants.map((r) => (
            <RestaurantCard 
              key={r.id} 
              restaurant={{
                id: r.id,
                name: r.name,
                description: r.description,
                image: r.photos[0], // Using first photo as main image
                address: r.address.fullAddress,
                phone: r.contact,
                cuisine: r.cuisine,
                costRating: r.costRating,
                rating: r.avgStarRating,
                status: r.status,
                openingTime: r.openingTime,
                closingTime: r.closingTime,
                approvalStatus: r.approvalStatus
              }}
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default DashboardPage; 