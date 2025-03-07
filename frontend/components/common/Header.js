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
export function Header() {

  function HandleClick() {

    
    
  }

  return (
    <div className="border-b">
      <div className="container mx-auto px-4 py-3 flex flex-wrap items-center gap-2">
        <div className="flex items-center mr-4">
          <span className="text-[#A31D1D] text-xl font-medium">SiteName</span>
        </div>

        <div className="flex flex-1 flex-wrap items-center gap-2">
          <div className="relative">
            <button className="flex items-center gap-1 border rounded px-3 py-2 bg-white">
              Location
            </button>
          </div>

          <div className="relative flex-1 max-w-xl">
            <div className="flex items-center border rounded overflow-hidden">
              <Search className="ml-2 h-4 w-4 text-gray-500" />
              <input
                type="text"
                placeholder="Search for Location, Restaurant or Cuisine"
                className="w-full px-2 py-2 outline-none"
              />
            </div>
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <User className="h-4 w-4 mr-2" />
            <span>#</span>
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <Calendar className="h-4 w-4 mr-2" />
            <span>Date</span>
          </div>

          <div className="flex items-center border rounded px-3 py-2 bg-white">
            <Clock className="h-4 w-4 mr-2" />
            <span>Time</span>
          </div>

          <button className="bg-[#8B2615] text-white px-6 py-2 rounded">
            Go
          </button>
        </div>

        <div className="flex items-center gap-2">
          <span className="text-sm text-gray-600">For Restaurants</span>
          <Link href="/signup">
            <button className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded"
            onClick={HandleClick}>
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
