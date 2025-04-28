"use client";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import Image from "next/image";
import { Star } from "lucide-react";

export default function SearchPage() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const searchParams = useSearchParams();

  // Filter states
  const [selectedCostRatings, setSelectedCostRatings] = useState([]);
  const [selectedRating, setSelectedRating] = useState(null);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [selectedLocations, setSelectedLocations] = useState([]);

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        setLoading(true);
        const location = searchParams.get("location") || "San Jose";
        const people = searchParams.get("people") || "1";
        const datetime = searchParams.get("datetime") || "2025-04-28T17:30:00-07:00";
        const name = searchParams.get("name") || "";

        const queryParams = new URLSearchParams({
          location,
          people,
          ...(name && { name }),
          datetime
        });

        const response = await fetch(`http://localhost:8080/api/restaurants/search?${queryParams}`);
        if (!response.ok) {
          throw new Error('Failed to fetch restaurants');
        }
        const data = await response.json();
        setRestaurants(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, [searchParams]);

  if (loading) {
    return (
      <div className="flex min-h-screen">
        <div className="w-64 p-4 border-r">
          <div className="animate-pulse">
            <div className="h-6 bg-gray-200 rounded w-3/4 mb-4"></div>
            <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
            <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
            <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
          </div>
        </div>
        <div className="flex-1 p-4">
          <div className="animate-pulse space-y-4">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-48 bg-gray-200 rounded"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex min-h-screen">
        <div className="w-64 p-4 border-r">
          {/* Filters */}
        </div>
        <div className="flex-1 p-4">
          <div className="text-red-500">Error: {error}</div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex min-h-screen">
      {/* Left Sidebar - Filters */}
      <div className="w-64 p-4 border-r">
        <div className="mb-6">
          <select className="w-full p-2 border rounded-md mb-4">
            <option>Sort</option>
            <option>Rating: High to Low</option>
            <option>Rating: Low to High</option>
          </select>
        </div>

        <div className="mb-6">
          <h3 className="font-semibold mb-3 flex items-center gap-2">
            <span className="material-icons text-xl">filter_list</span>
            Filter
          </h3>
          <div className="space-x-2 mb-4">
            <button className="px-3 py-1 border rounded-md hover:bg-gray-50">$$</button>
            <button className="px-3 py-1 border rounded-md hover:bg-gray-50">$$$</button>
            <button className="px-3 py-1 border rounded-md hover:bg-gray-50">$$$$</button>
          </div>
        </div>

        <div className="mb-6">
          <h3 className="font-semibold mb-3">Rating</h3>
          <div className="space-y-2">
            {[5, 4, 3, 2, 1].map((rating) => (
              <label key={rating} className="flex items-center gap-2">
                <input type="checkbox" className="rounded" />
                <div className="flex text-yellow-400">
                  {[...Array(rating)].map((_, i) => (
                    <Star key={i} className="h-4 w-4 fill-current" />
                  ))}
                  {[...Array(5 - rating)].map((_, i) => (
                    <Star key={i} className="h-4 w-4" />
                  ))}
                </div>
                <span>& above</span>
              </label>
            ))}
          </div>
        </div>

        <div className="mb-6">
          <h3 className="font-semibold mb-3 flex items-center gap-2">
            <span className="material-icons text-xl">restaurant</span>
            Categories
          </h3>
          <div className="space-y-2">
            {["Italian", "Chinese", "Mexican", "Indian"].map((category) => (
              <label key={category} className="flex items-center gap-2">
                <input type="checkbox" className="rounded" />
                <span>{category}</span>
              </label>
            ))}
          </div>
          <button className="text-[#8B2615] mt-2">See More +</button>
        </div>

        <div className="mb-6">
          <h3 className="font-semibold mb-3 flex items-center gap-2">
            <span className="material-icons text-xl">location_on</span>
            Location
          </h3>
          <div className="space-y-2">
            {["San Jose", "Santa Clara", "Sunnyvale", "Mountain View"].map((location) => (
              <label key={location} className="flex items-center gap-2">
                <input type="checkbox" className="rounded" />
                <span>{location}</span>
              </label>
            ))}
          </div>
        </div>
      </div>

      {/* Main Content - Restaurant List */}
      <div className="flex-1 p-4">
        {restaurants.length === 0 ? (
          <div className="flex flex-col items-center justify-center h-full">
            <h2 className="text-2xl font-semibold text-gray-600 mb-2">No restaurants found</h2>
            <p className="text-gray-500">Try adjusting your search criteria</p>
          </div>
        ) : (
          <div className="space-y-4">
            {restaurants.map((restaurant) => (
              <Link
                key={restaurant.restaurantId}
                href={`/restaurant/${restaurant.restaurantId}`}
                className="block border rounded-lg overflow-hidden hover:shadow-md transition-shadow"
              >
                <div className="flex">
                  <div className="w-48 h-48 relative flex-shrink-0">
                    {restaurant.photos && restaurant.photos.length > 0 ? (
                      <Image
                        src={restaurant.photos[0]}
                        alt={restaurant.restaurantName}
                        fill
                        className="object-cover"
                      />
                    ) : (
                      <div className="w-full h-full bg-gray-200 flex items-center justify-center">
                        <span className="text-gray-400">No photo</span>
                      </div>
                    )}
                  </div>
                  <div className="p-4 flex-1">
                    <h2 className="text-xl font-semibold mb-2">{restaurant.restaurantName}</h2>
                    <div className="flex items-center gap-2 mb-2">
                      <span className="text-gray-600">{restaurant.cuisine}</span>
                      <span className="text-gray-600">
                        {restaurant.costRating === 1 ? '$' : restaurant.costRating === 2 ? '$$' : '$$$'}
                      </span>
                      <div className="flex items-center">
                        <div className="flex text-yellow-400">
                          {[...Array(5)].map((_, i) => (
                            <Star
                              key={i}
                              className={`h-4 w-4 ${i < Math.floor(restaurant.avgRating) ? 'fill-current' : ''}`}
                            />
                          ))}
                        </div>
                        <span className="ml-1 text-sm text-gray-600">
                          {restaurant.avgRating} ({restaurant.totalReviews} reviews)
                        </span>
                      </div>
                      <span className="text-gray-600">{restaurant.bookedToday} booked today</span>
                    </div>

                    {restaurant.photos && restaurant.photos.length > 1 && (
                      <div className="flex gap-2 mb-3 overflow-x-auto">
                        {restaurant.photos.slice(1).map((photo, index) => (
                          <div key={index} className="w-16 h-16 relative flex-shrink-0">
                            <Image
                              src={photo}
                              alt={`${restaurant.restaurantName} photo ${index + 2}`}
                              fill
                              className="object-cover rounded-md"
                            />
                          </div>
                        ))}
                      </div>
                    )}

                    <div className="mt-4">
                      <h3 className="text-sm font-medium mb-2">Available Times:</h3>
                      <div className="flex flex-wrap gap-2">
                        {restaurant.availableTimes.map((time, timeIndex) => (
                          <span
                            key={timeIndex}
                            className="px-3 py-1 bg-[#8B2615] text-white text-sm rounded-full hover:bg-[#a13425] transition-colors"
                          >
                            {time}
                          </span>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
