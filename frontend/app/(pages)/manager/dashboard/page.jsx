"use client";
import React, { useEffect, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import RestaurantCard from "../components/RestaurantCard";
import { useRouter } from "next/navigation";
import { getRestaurantsByManager } from "@/constants/apis";

const DashboardPage = () => {
  const { user, isLoggedIn, isLoading: isAuthLoading } = useAuth();
  const router = useRouter();
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRestaurants = async () => {
      if (!user?.id) return;

      try {
        const data = await getRestaurantsByManager(user.id);
        setRestaurants(data);
      } catch (err) {
        console.error('Error fetching restaurants:', err);
        setError('Failed to load restaurants. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    if (!isAuthLoading) {
      if (isLoggedIn && user) {
        fetchRestaurants();
      } else {
        router.push('/login');
      }
    }
  }, [user?.id, isLoggedIn, router, isAuthLoading]);

  if (isAuthLoading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-slate-500">Loading...</div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-slate-500">Loading restaurants...</div>
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