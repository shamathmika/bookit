"use client";
import React from "react";
import { useRestaurantContext } from "../context/RestaurantContext";
import RestaurantCard from "../components/RestaurantCard";

const DashboardPage = () => {
  const { restaurants } = useRestaurantContext();

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">My Restaurants</h1>
      {restaurants.length === 0 ? (
        <div className="text-slate-500">No restaurants yet. Add one to get started!</div>
      ) : (
        <div className="grid gap-6 lg:grid-cols-3">
          {restaurants.map((r) => (
            <RestaurantCard key={r.id} restaurant={r} />
          ))}
        </div>
      )}
    </div>
  );
};

export default DashboardPage; 