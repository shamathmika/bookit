"use client"

import { useState } from "react"
import Link from "next/link"

export default function AdminRestaurants() {
  const [restaurants, setRestaurants] = useState([
    { id: 1, name: "Restaurant", location: "city", status: "pending" },
    { id: 2, name: "Restaurant", location: "city", status: "pending" },
    { id: 3, name: "Restaurant", location: "city", status: "pending" },
    { id: 4, name: "Restaurant", location: "city", status: "pending" },
    { id: 5, name: "Restaurant", location: "city", status: "pending" },
    { id: 6, name: "Restaurant", location: "city", status: "pending" },
    { id: 7, name: "Restaurant", location: "city", status: "Approved" },
  ])

  const handleApprove = (id) => {
    setRestaurants(
      restaurants.map((restaurant) => (restaurant.id === id ? { ...restaurant, status: "Approved" } : restaurant)),
    )
  }

  const handleReject = (id) => {
    setRestaurants(restaurants.filter((restaurant) => restaurant.id !== id))
  }

  const handleRemove = (id) => {
    setRestaurants(restaurants.filter((restaurant) => restaurant.id !== id))
  }

  return (
    <div className="flex min-h-screen bg-white">
      {/* Sidebar */}
      <div className="w-64 bg-[#1a1a1a] text-white flex flex-col">
        <div className="p-6 border-b border-gray-700">
          <h1 className="text-xl font-medium">Admin</h1>
        </div>

        <nav className="flex-1">
          <Link href="/admin" className="flex items-center px-6 py-4 hover:bg-gray-800">
            Dashboard
          </Link>
          <Link href="/admin/restaurants" className="flex items-center px-6 py-4 bg-[#b8a8a8] text-black">
            Restaurants
          </Link>
        </nav>
      </div>

      {/* Main content */}
      <div className="flex-1 overflow-auto">
        <div className="p-8">
          <h1 className="text-3xl font-bold mb-8">Requests</h1>

          <div className="bg-gray-200 rounded-lg overflow-hidden">
            {/* Table header */}
            <div className="grid grid-cols-4 gap-4 bg-gray-600 text-white p-4">
              <div className="font-medium">Name</div>
              <div className="font-medium">Location</div>
              <div className="font-medium">status</div>
              <div className="font-medium">action</div>
            </div>

            {/* Table rows */}
            {restaurants.map((restaurant) => (
              <div key={restaurant.id} className="grid grid-cols-4 gap-4 p-4 border-b border-gray-300">
                <div>{restaurant.name}</div>
                <div>{restaurant.location}</div>
                <div>{restaurant.status}</div>
                <div className="flex gap-2">
                  {restaurant.status === "pending" ? (
                    <>
                      <button
                        onClick={() => handleApprove(restaurant.id)}
                        className="bg-[#8B2615] text-white px-3 py-1 rounded text-sm"
                      >
                        Approve
                      </button>
                      <button
                        onClick={() => handleReject(restaurant.id)}
                        className="bg-[#8B2615] text-white px-3 py-1 rounded text-sm"
                      >
                        Reject
                      </button>
                    </>
                  ) : (
                    <button
                      onClick={() => handleRemove(restaurant.id)}
                      className="bg-[#8B2615] text-white px-3 py-1 rounded text-sm"
                    >
                      Remove
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

