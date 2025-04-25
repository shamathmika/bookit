'use client';

import { useState, useEffect } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import { FaSearch, FaMapMarkerAlt, FaUtensils } from 'react-icons/fa';
import { BsPeopleFill, BsCalendarDate, BsClock } from 'react-icons/bs';
import { Header } from '@/components/common/Header';
import {
  Search,
  User,
  Calendar,
  Clock,
  ChevronLeft,
  ChevronRight,
  Github,
  Twitter,
} from "lucide-react";

const SearchPage = () => {
  const [location, setLocation] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [partySize, setPartySize] = useState('');
  const [date, setDate] = useState('');
  const [time, setTime] = useState('');
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    priceRange: [],
    rating: [],
    categories: [],
    locations: []
  });

  const handleSearch = async () => {
    setLoading(true);
    setError(null);
    try {
      const queryParams = new URLSearchParams();
      
      // Only add parameters if they have values
      if (searchQuery) queryParams.append('name', searchQuery);
      if (location) queryParams.append('location', location);
      if (partySize) queryParams.append('people', partySize);
      
      // Format datetime properly
      if (date && time) {
        const combined = new Date(
          date.getFullYear(), date.getMonth(), date.getDate(),
          time.getHours(),   time.getMinutes()
        );
        queryParams.append('datetime', combined.toISOString()); 
      }

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

  // Get initial search parameters from URL
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    setSearchQuery(params.get('name') || '');
    setLocation(params.get('location') || '');
    setPartySize(params.get('people') || '');
    const datetime = params.get('datetime');
    if (datetime) {
      const [datePart, timePart] = datetime.split('T');
      setDate(datePart);
      setTime(timePart);
    }
    handleSearch();
  }, []);

  return (
    <div className="min-h-screen bg-white">

      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex gap-8">
          {/* Filters Section */}
          <div className="w-64 flex-shrink-0">
            <div className="mb-6">
              <button className="w-full border rounded-md p-2 text-left flex justify-between items-center">
                Sort
                <span className="text-gray-400">▼</span>
              </button>
            </div>

            <div className="mb-6">
              <h3 className="font-semibold mb-2 flex items-center">
                <FaUtensils className="mr-2" /> Filter
              </h3>
              <div className="flex gap-2 mb-4">
                {['$$', '$$$', '$$$$'].map((price) => (
                  <button
                    key={price}
                    className={`border rounded-md px-3 py-1 ${
                      filters.priceRange.includes(price) ? 'bg-red-100 border-red-500' : ''
                    }`}
                    onClick={() => {
                      setFilters(prev => ({
                        ...prev,
                        priceRange: prev.priceRange.includes(price)
                          ? prev.priceRange.filter(p => p !== price)
                          : [...prev.priceRange, price]
                      }));
                    }}
                  >
                    {price}
                  </button>
                ))}
              </div>
            </div>

            <div className="mb-6">
              <h3 className="font-semibold mb-2">Rating</h3>
              <div className="space-y-2">
                {[5,4,3,2,1].map((rating) => (
                  <div key={rating} className="flex items-center">
                    <input
                      type="checkbox"
                      className="mr-2"
                      checked={filters.rating.includes(rating)}
                      onChange={() => {
                        setFilters(prev => ({
                          ...prev,
                          rating: prev.rating.includes(rating)
                            ? prev.rating.filter(r => r !== rating)
                            : [...prev.rating, rating]
                        }));
                      }}
                    />
                    {'★'.repeat(rating)}{'☆'.repeat(5-rating)} & above
                  </div>
                ))}
              </div>
            </div>

            <div className="mb-6">
              <h3 className="font-semibold mb-2 flex items-center">
                <FaUtensils className="mr-2" /> Categories
              </h3>
              <div className="space-y-2">
                {['Italian', 'Chinese', 'Mexican', 'Indian'].map((cat) => (
                  <div key={cat} className="flex items-center">
                    <input
                      type="checkbox"
                      className="mr-2"
                      checked={filters.categories.includes(cat)}
                      onChange={() => {
                        setFilters(prev => ({
                          ...prev,
                          categories: prev.categories.includes(cat)
                            ? prev.categories.filter(c => c !== cat)
                            : [...prev.categories, cat]
                        }));
                      }}
                    />
                    {cat}
                  </div>
                ))}
                <button className="text-red-800">See More +</button>
              </div>
            </div>

            <div className="mb-6">
              <h3 className="font-semibold mb-2 flex items-center">
                <FaMapMarkerAlt className="mr-2" /> Location
              </h3>
              <div className="space-y-2">
                {['San Jose', 'Santa Clara', 'Sunnyvale', 'Mountain View'].map((loc) => (
                  <div key={loc} className="flex items-center">
                    <input
                      type="checkbox"
                      className="mr-2"
                      checked={filters.locations.includes(loc)}
                      onChange={() => {
                        setFilters(prev => ({
                          ...prev,
                          locations: prev.locations.includes(loc)
                            ? prev.locations.filter(l => l !== loc)
                            : [...prev.locations, loc]
                        }));
                      }}
                    />
                    {loc}
                  </div>
                ))}
                <button className="text-red-800">See More +</button>
              </div>
            </div>
          </div>

          {/* Results Section */}
          <div className="flex-1">
            {loading ? (
              <div className="text-center py-8">Loading...</div>
            ) : error ? (
              <div className="text-red-500 text-center py-8">{error}</div>
            ) : (
              <div className="grid gap-6">
                {restaurants.map((restaurant) => (
                  <div key={restaurant.restaurantName} className="border rounded-lg p-6 hover:shadow-lg transition-shadow">
                    <div className="flex justify-between items-start">
                      <div>
                        <h2 className="text-xl font-semibold mb-2">{restaurant.restaurantName}</h2>
                        <div className="flex items-center gap-4 text-gray-600 mb-4">
                          <span className="flex items-center">
                            <FaUtensils className="mr-1" /> {restaurant.cuisine}
                          </span>
                          <span>{'$'.repeat(restaurant.costRating)}</span>
                          <span className="flex items-center">
                            ⭐ {restaurant.avgRating} ({restaurant.totalReviews} reviews)
                          </span>
                          <span className="flex items-center">
                            <BsPeopleFill className="mr-1" /> {restaurant.bookedToday} booked today
                          </span>
                        </div>
                      </div>
                    </div>
                    <div className="mt-4">
                      <h3 className="font-medium mb-2">Available Times:</h3>
                      <div className="flex flex-wrap gap-2">
                        {restaurant.availableTimes.map((time) => (
                          <button
                            key={time}
                            className="border rounded-md px-3 py-1 hover:bg-red-50 hover:border-red-500 transition-colors"
                          >
                            {time}
                          </button>
                        ))}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
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
};

export default SearchPage;
