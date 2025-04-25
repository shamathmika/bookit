"use client";
import { useEffect, useState } from "react";
import {
  Search,
  User,
  Calendar,
  Clock,
  ChevronLeft,
  ChevronRight,
  Github,
  Twitter,
  Star,
} from "lucide-react";
import Image from "next/image";

import { RestaurantCard } from "@/components/common/RestaurantCard";
import { StarIcon } from "@/components/common/StarIcon";

import { Restaurants } from "@/constants/constants";

import { Categories } from "@/constants/constants";

export default function Component() {
  const [availableRestaurants, setAvailableRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAvailableRestaurants = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/restaurants/available-tables');
        if (!response.ok) {
          throw new Error('Failed to fetch available restaurants');
        }
        const data = await response.json();
        setAvailableRestaurants(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchAvailableRestaurants();
  }, []);

  if (loading) {
    return (
      <div className="flex flex-col min-h-screen bg-white">
        <main className="flex-1 container mx-auto px-4 py-6">
          <div className="animate-pulse">
            <div className="h-8 bg-gray-200 rounded w-1/3 mb-6"></div>
            <div className="flex space-x-4 overflow-x-auto pb-4">
              {[1, 2, 3, 4].map((i) => (
                <div key={i} className="w-64 h-48 bg-gray-200 rounded"></div>
              ))}
            </div>
          </div>
        </main>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col min-h-screen bg-white">
        <main className="flex-1 container mx-auto px-4 py-6">
          <div className="text-red-500">Error: {error}</div>
        </main>
      </div>
    );
  }

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <main className="flex-1 container mx-auto px-4 py-6">
        <h1 className="text-4xl font-bold mb-6">Currently Available Tables</h1>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {availableRestaurants.map((restaurant, index) => (
            <div key={index} className="border rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow">
              <div className="p-4">
                <h2 className="text-xl font-semibold mb-2">{restaurant.restaurantName}</h2>
                <div className="flex items-center mb-2">
                  <div className="flex text-yellow-400">
                    {[...Array(5)].map((_, i) => (
                      <Star 
                        key={i} 
                        className={`h-4 w-4 ${i < Math.floor(restaurant.avgRating) ? 'fill-current' : ''}`}
                      />
                    ))}
                  </div>
                  <span className="text-sm text-gray-600 ml-1">
                    ({restaurant.avgRating.toFixed(1)})
                  </span>
                </div>
                <div className="text-sm text-gray-600 mb-2">
                  {restaurant.cuisine} • {restaurant.costRating === 1 ? '$' : restaurant.costRating === 2 ? '$$' : '$$$'}
                </div>
                <div className="text-sm text-gray-600 mb-2">
                  {restaurant.totalReviews} reviews • {restaurant.bookedToday} booked today
                </div>
                <div className="mt-4">
                  <h3 className="text-sm font-medium mb-2">Available Times:</h3>
                  <div className="flex flex-wrap gap-2">
                    {restaurant.availableTimes.map((time, timeIndex) => (
                      <span 
                        key={timeIndex}
                        className="px-3 py-1 bg-[#8B2615] text-white text-sm rounded-full"
                      >
                        {time}
                      </span>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="text-4xl font-bold mb-6 mt-12">
          Categories
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {Categories.map((category) => (
            <CategorySection key={category.id} category={category} />
          ))}
        </div>
      </main>

      <footer className="text-center py-4 text-sm text-gray-600 border-t">
        <div>(C) 2025 Maverick, Inc</div>
        <div className="flex justify-center gap-4 mt-2">
          <Github size={16} />
          <Twitter size={16} />
        </div>
      </footer>
    </div>
  );
}

function CategorySection({ category }) {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/restaurants/search?cuisine=${category.name}`);
        if (!response.ok) {
          throw new Error('Failed to fetch restaurants');
        }
        const data = await response.json();
        setRestaurants(data.slice(0, 4)); // Show only first 4 restaurants
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, [category.name]);

  if (loading) {
    return (
      <div className="border rounded-md p-4">
        <h2 className="text-xl text-[#8B2615] font-medium mb-4">{category.name}</h2>
        <div className="animate-pulse">
          {[1, 2, 3, 4].map((i) => (
            <div key={i} className="flex gap-3 py-3 border-t">
              <div className="w-15 h-15 bg-gray-200 rounded"></div>
              <div className="flex-1">
                <div className="h-4 bg-gray-200 rounded w-1/3 mb-2"></div>
                <div className="h-4 bg-gray-200 rounded w-1/2"></div>
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="border rounded-md p-4">
        <h2 className="text-xl text-[#8B2615] font-medium mb-4">{category.name}</h2>
        <div className="text-red-500">Error: {error}</div>
      </div>
    );
  }

  return (
    <div className="border rounded-md p-4">
      <h2 className="text-xl text-[#8B2615] font-medium mb-4">{category.name}</h2>

      {restaurants.map((restaurant) => (
        <div key={restaurant.id} className="flex gap-3 py-3 border-t">
          <div className="flex-shrink-0">
            <Image
              src={restaurant.imageUrl || "https://plus.unsplash.com/premium_photo-1675344317686-118cc9f89f8a?q=80&w=2940&auto=format&fit=crop"}
              alt="Restaurant Image"
              width={60}
              height={60}
              className="rounded"
            />
          </div>

          <div>
            <h3 className="font-medium">{restaurant.name}</h3>
            <div className="flex items-center text-sm">
              <div className="flex text-yellow-400">
                {[...Array(5)].map((_, i) => (
                  <Star 
                    key={i} 
                    className={`h-3 w-3 ${i < Math.floor(restaurant.avgRating) ? 'fill-current' : ''}`}
                  />
                ))}
              </div>
              <span className="text-xs text-gray-500 ml-1">
                ({restaurant.avgRating})
              </span>
            </div>

            <div className="text-xs text-gray-500">
              {restaurant.cuisine} | {restaurant.costRating === 1 ? '$' : restaurant.costRating === 2 ? '$$' : '$$$'} | {restaurant.region}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}

function ChevronDown() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="16"
      height="16"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <polyline points="6 9 12 15 18 9" />
    </svg>
  );
}
