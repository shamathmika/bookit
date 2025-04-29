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
import { useAuth } from "@/context/AuthContext";

export function Header() {
  
  const router = useRouter();
  const pathname = usePathname();
  const [userRole, setUserRole] = useState(null);

  // New state for login status


  // Existing state
  const [date, setDate] = useState(new Date());
  const [time, setTime] = useState(new Date());
  const [location, setLocation] = useState("");
  const [guestCount, setGuestCount] = useState(2);
  const [searchQuery, setSearchQuery] = useState("");

  const { isLoggedIn, logout } = useAuth();

  useEffect(() => {
    if (isLoggedIn) {
      const token = localStorage.getItem("token");
      if (token) {
        try {
          const base64Url = token.split('.')[1];
          const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
          }).join(''));
          const payload = JSON.parse(jsonPayload);
          setUserRole(payload.role);
        } catch (error) {
          console.error("Error parsing token:", error);
        }
      }
    } else {
      setUserRole(null);
    }
  }, [isLoggedIn]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    logout();
    setUserRole(null);
    router.push("/home");
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
      const params = new URLSearchParams();
      if (searchQuery) params.append("name", searchQuery);
      if (location) params.append("location", location);
      if (guestCount) params.append("people", guestCount);
      const combined = new Date(
        date.getFullYear(), date.getMonth(), date.getDate(),
        time.getHours(), time.getMinutes()
      );
      params.append("datetime", combined.toISOString());

      const url = `http://localhost:8080/api/restaurants/search?${params.toString()}`;
      const res = await fetch(url);
      if (!res.ok) throw new Error("Search failed");

      router.push(`/search?${params.toString()}`);
    } catch (err) {
      console.error("Search error:", err);
    }
  };

  return (
    <div className="border-b">
      <div className="container mx-auto px-4 py-3 flex flex-wrap items-center gap-2">
        {/* Logo */}
        <Link href="/home">
          <span className="text-[#A31D1D] text-xl font-medium cursor-pointer">
            BookIt
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
            <>
              {userRole === "ROLE_CUSTOMER" && (
                <Link href="/user">
                  <button className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded mr-2">
                    Profile
                  </button>
                </Link>
              )}
              {userRole === "ROLE_MANAGER" && (
                <Link href="/manage">
                  <button className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded mr-2">
                    Manage
                  </button>
                </Link>
              )}
              <button
                onClick={handleLogout}
                className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded"
              >
                Logout
              </button>
            </>
          ) : (
            <Link href="/login">
              <button className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded">
                Sign In
              </button>
            </Link>
          )}
        </div>
      </div>
    </div>
  );
}
