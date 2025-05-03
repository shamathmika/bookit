"use client";
import React, { useState, useEffect } from "react";
import { useAuth } from "@/context/AuthContext";
import RestaurantCard from "../components/RestaurantCard";

const DashboardPage = () => {
  const { user } = useAuth();
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

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

  if (loading) {
    return <div className="text-slate-500">Loading restaurants...</div>;
  }

  if (error) {
    return <div className="text-red-500">Error: {error}</div>;
  }

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">My Restaurants</h1>
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
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default DashboardPage; 