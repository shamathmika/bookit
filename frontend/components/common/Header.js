"use client";
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
import Link from "next/link";
import { useState } from "react";
import "react-datepicker/dist/react-datepicker.css";
import DatePicker from "react-datepicker";
import { useRouter } from "next/navigation";

export function Header() {
  const router = useRouter();
  const [date, setDate] = useState(new Date());
  const [time, setTime] = useState(new Date());
  const [location, setLocation] = useState("");
  const [guestCount, setGuestCount] = useState(2);
  const [searchQuery, setSearchQuery] = useState("");

  function HandleClick() {
    // Handle sign in click
  }

  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
  };

  const handleGuestCountChange = (e) => {
    const count = parseInt(e.target.value);
    if (count > 0) {
      setGuestCount(count);
    }
  };

  const handleSearchSubmit = async () => {
    try {
      const searchParams = {
        query: searchQuery,
        guestCount,
        date: date.toISOString().split('T')[0],
        time: time.toLocaleTimeString('en-US', { hour12: false }),
        location
      };

      // Make API call to backend
      const response = await fetch('/api/search', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(searchParams),
      });

      if (!response.ok) {
        throw new Error('Search failed');
      }

      const data = await response.json();
      
      // Navigate to search page with results
      router.push(`/search?query=${encodeURIComponent(searchQuery)}&guests=${guestCount}&date=${date.toISOString().split('T')[0]}&time=${time.toLocaleTimeString('en-US', { hour12: false })}&location=${encodeURIComponent(location)}`);
    } catch (error) {
      console.error('Search error:', error);
      // Handle error appropriately
    }
  };

  return (
    <div className="border-b">
      <div className="container mx-auto px-4 py-3 flex flex-wrap items-center gap-2">
        <div className="flex items-center mr-4">
          <Link href="/home">
            <span className="text-[#A31D1D] text-xl font-medium">SiteName</span>
          </Link>
        </div>

        <div className="flex flex-1 flex-wrap items-center gap-2">
          <div className="relative">
            <button className="flex items-center gap-1 border rounded px-3 py-2 bg-white">
              <select
                className="outline-none bg-transparent"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              >
                <option value="">Select Location</option>
                <option value="new-york">New York</option>
                <option value="los-angeles">Los Angeles</option>
                <option value="chicago">Chicago</option>
                <option value="houston">Houston</option>
              </select>
            </button>
          </div>

          <div className="relative flex-1 max-w-xl">
            <div className="flex items-center border rounded overflow-hidden">
              <Search className="ml-2 h-4 w-4 text-gray-500" />
              <input
                type="text"
                placeholder="Search for Location, Restaurant or Cuisine"
                className="w-full px-2 py-2 outline-none"
                value={searchQuery}
                onChange={handleSearch}
              />
            </div>
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <User className="h-4 w-4 mr-2" />
            <input
              type="number"
              min="1"
              value={guestCount}
              onChange={handleGuestCountChange}
              className="w-12 outline-none bg-transparent"
            />
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <Calendar className="h-4 w-4 mr-2" />
            <DatePicker
              selected={date}
              onChange={(date) => setDate(date)}
              className="w-24 outline-none bg-transparent"
              dateFormat="MM/dd/yyyy"
            />
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <Clock className="h-4 w-4 mr-2" />
            <DatePicker
              selected={time}
              onChange={(time) => setTime(time)}
              showTimeSelect
              showTimeSelectOnly
              timeIntervals={15}
              timeCaption="Time"
              dateFormat="h:mm aa"
              className="w-24 outline-none bg-transparent"
            />
          </div>
          <button 
            className="bg-[#8B2615] text-white px-6 py-2 rounded"
            onClick={handleSearchSubmit}
          >
            Go
          </button>
        </div>

        <div className="flex items-center gap-2">
          <span className="text-sm text-gray-600">For Restaurants</span>
          <Link href="/signup">
            <button
              className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded"
              onClick={HandleClick}
            >
              Sign In
            </button>
          </Link>
        </div>
      </div>
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
