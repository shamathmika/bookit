"use client";
import {
  Search,
  User,
  Calendar,
  Clock,
} from "lucide-react";
import Link from "next/link";
import { useState, useEffect } from "react";
import "react-datepicker/dist/react-datepicker.css";
import DatePicker from "react-datepicker";
import { usePathname, useRouter } from "next/navigation";

export function Header() {
  const router = useRouter();
  const pathname = usePathname();
  // New state for login status
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Existing state
  const [date, setDate] = useState(new Date());
  const [time, setTime] = useState(new Date());
  const [location, setLocation] = useState("");
  const [guestCount, setGuestCount] = useState(2);
  const [searchQuery, setSearchQuery] = useState("");

  // On mount, check for a saved token
  useEffect(() => {
    const token = localStorage.getItem("token");
    setIsLoggedIn(!!token);
  },[pathname]);

  // Call this after a successful sign-in: e.g. in your sign-in page,
  // save `localStorage.setItem("authToken", token)` then router.push back here.
  // ...

  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsLoggedIn(false);
    router.push("/home");   // or wherever you want
  };

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
      const queryParams = new URLSearchParams();
      if (searchQuery) queryParams.append("name", searchQuery);
      if (location) queryParams.append("location", location);
      if (guestCount) queryParams.append("people", guestCount.toString());

      if (date && time) {
        const combined = new Date(
          date.getFullYear(), date.getMonth(), date.getDate(),
          time.getHours(), time.getMinutes()
        );
        queryParams.append("datetime", combined.toISOString());
      }

      const url = `/api/restaurants/search?${queryParams.toString()}`;
      const response = await fetch(`http://localhost:8080${url}`);
      if (!response.ok) throw new Error("Search failed");

      router.push(`/search?${queryParams.toString()}`);
    } catch (error) {
      console.error("Search error:", error);
    }
  };

  return (
    <div className="border-b">
      <div className="container mx-auto px-4 py-3 flex flex-wrap items-center gap-2">
        {/* Logo */}
        <Link href="/home">
          <span className="text-[#A31D1D] text-xl font-medium cursor-pointer">
            SiteName
          </span>
        </Link>

        {/* Search controls */}
        <div className="flex flex-1 flex-wrap items-center gap-2">
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
              min={1}
              value={guestCount}
              onChange={handleGuestCountChange}
              className="w-12 outline-none bg-transparent"
            />
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <Calendar className="h-4 w-4 mr-2" />
            <DatePicker
              selected={date}
              onChange={(d) => setDate(d)}
              className="w-24 outline-none bg-transparent"
              dateFormat="MM/dd/yyyy"
            />
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <Clock className="h-4 w-4 mr-2" />
            <DatePicker
              selected={time}
              onChange={(t) => setTime(t)}
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

        {/* Auth controls */}
        <div className="flex items-center gap-2">
          {isLoggedIn ? (
            <button
              onClick={handleLogout}
              className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded"
            >
              Logout
            </button>
          ) : (
            <Link href="/signup">
              <button
                onClick={() => {}}
                className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded"
              >
                Sign In
              </button>
            </Link>
          )}
        </div>
      </div>
    </div>
  );
}
