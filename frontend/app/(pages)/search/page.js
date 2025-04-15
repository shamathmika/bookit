'use client';

import { useState } from 'react';
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
  
  // Mock data for restaurants
  const restaurants = [
    {
      id: 1,
      name: "Restaurant",
      cuisine: "Cuisine",
      region: "Region",
      rating: 4.5,
      bookings: 8,
      availableTimes: ["-30", "Time", "+30"],
    },
    // Add more mock restaurants as needed
  ];

  return (
    <div className="min-h-screen bg-white">
      {/* Header/Navigation */}


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
                <button className="border rounded-md px-3 py-1">$$</button>
                <button className="border rounded-md px-3 py-1">$$$</button>
                <button className="border rounded-md px-3 py-1">$$$$</button>
              </div>
            </div>

            <div className="mb-6">
              <h3 className="font-semibold mb-2">Rating</h3>
              <div className="space-y-2">
                {[5,4,3,2,1].map((rating) => (
                  <div key={rating} className="flex items-center">
                    <input type="checkbox" className="mr-2" />
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
                {['Category', 'Category', 'Category', 'Category'].map((cat, idx) => (
                  <div key={idx} className="flex items-center">
                    <input type="checkbox" className="mr-2" />
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
                {['Category', 'Category', 'Category', 'Category'].map((cat, idx) => (
                  <div key={idx} className="flex items-center">
                    <input type="checkbox" className="mr-2" />
                    {cat}
                  </div>
                ))}
                <button className="text-red-800">See More +</button>
              </div>
            </div>
          </div>

          {/* Restaurant Listings */}
          <div className="flex-1">
            <h2 className="text-xl font-semibold mb-6"># restaurants available</h2>
            
            <div className="space-y-6">
              {restaurants.map((restaurant) => (
                <div key={restaurant.id} className="border rounded-lg p-4 flex">
                  <div className="w-48 h-32 bg-gray-200 rounded-md mr-4">
                    {/* Restaurant Image Placeholder */}
                  </div>
                  
                  <div className="flex-1">
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="text-lg font-semibold">{restaurant.name}</h3>
                        <div className="text-sm text-gray-600">
                          {restaurant.cuisine} | {restaurant.region}
                        </div>
                        <div className="flex items-center mt-1">
                          {'★'.repeat(Math.floor(restaurant.rating))}
                          <span className="text-sm text-gray-600 ml-1">
                            (Round down review #)
                          </span>
                        </div>
                        <div className="text-sm text-gray-600 mt-1">
                          Booked {restaurant.bookings} times today
                        </div>
                      </div>
                    </div>
                    
                    <div className="flex gap-2 mt-4">
                      {restaurant.availableTimes.map((time, idx) => (
                        <button
                          key={idx}
                          className="border rounded-md px-4 py-1 text-sm hover:bg-gray-50"
                        >
                          {time}
                        </button>
                      ))}
                    </div>
                  </div>
                </div>
              ))}
            </div>
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
