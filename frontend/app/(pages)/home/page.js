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
import Image from "next/image";

import { RestaurantCard } from "@/components/common/RestaurantCard";
import { StarIcon } from "@/components/common/StarIcon";

import { Restaurants } from "@/constants/constants";

import { Categories } from "@/constants/constants";

export default function Component() {
  return (
    <div className="flex flex-col min-h-screen bg-white">
      <main className="flex-1 container mx-auto px-4 py-6">
        <h1 className="text-4xl font-bold mb-6">Currently Available Tables</h1>
        <div className="relative">
          <div className="flex overflow-x-auto gap-4 pb-4">
            {Restaurants.map((restaurant) => (
              <RestaurantCard key={restaurant.id} {...restaurant} />
            ))}
          </div>

          {/* <button className="absolute left-0 top-1/2 -translate-y-1/2 bg-white rounded-full p-2 shadow-md">
            <ChevronLeft className="h-5 w-5" />
          </button>

          <button className="absolute right-0 top-1/2 -translate-y-1/2 bg-white rounded-full p-2 shadow-md">
            <ChevronRight className="h-5 w-5" />
          </button> */}
        </div>
        <div className="text-4xl font-bold mb-6 mt-5">
        Categories
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-12">
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
  return (
    <div className="border rounded-md p-4">
      <h2 className="text-xl text-[#8B2615] font-medium mb-4">{category.name}</h2>

      {[1, 2, 3, 4].map((item) => (
        <div key={item} className="flex gap-3 py-3 border-t">
          <div className="flex-shrink-0">
            <Image
              src="https://plus.unsplash.com/premium_photo-1675344317686-118cc9f89f8a?q=80&w=2940&auto=format&fit=crop"
              alt={category.name}
              width={60}
              height={60}
              className="rounded"
            />
          </div>

          <div>
            <h3 className="font-medium">Restaurant</h3>
            <div className="flex items-center text-sm">
              <div className="flex text-yellow-400">
                {[1, 2, 3, 4, 5].map((star) => (
                  <StarIcon key={star} className="h-3 w-3" />
                ))}
              </div>
              <span className="text-xs text-gray-500 ml-1">
                (Round down review #)
              </span>
            </div>

            <div className="text-xs text-gray-500">Cuisine | $$ | Region</div>
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
