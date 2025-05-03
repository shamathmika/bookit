"use client";
import React from "react";
import PropTypes from "prop-types";
import StatBadge from "./StatBadge";

const RestaurantCard = ({ restaurant }) => {
  return (
    <div className="bg-white rounded-xl shadow flex flex-col overflow-hidden">
      <div className="relative h-48">
        {restaurant.image ? (
          <img 
            src={restaurant.image} 
            alt={restaurant.name}
            className="w-full h-full object-cover"
          />
        ) : (
          <div className="bg-slate-700 text-white flex items-center justify-center h-full rounded-t-xl text-lg font-semibold">
            No Image
          </div>
        )}
        {restaurant.approvalStatus && (
          <div className={`absolute top-2 right-2 px-2 py-1 rounded text-sm font-medium ${
            restaurant.approvalStatus === "APPROVED" ? "bg-green-100 text-green-800" :
            restaurant.approvalStatus === "PENDING" ? "bg-yellow-100 text-yellow-800" :
            "bg-red-100 text-red-800"
          }`}>
            {restaurant.approvalStatus}
          </div>
        )}
      </div>
      <div className="p-5 flex-1 flex flex-col gap-2">
        <div className="font-bold text-lg">{restaurant.name}</div>
        <div className="text-slate-500 text-sm">{restaurant.cuisine}</div>
        <div className="text-slate-500 text-sm mb-2">{restaurant.address}</div>
        <div className="flex gap-2 mb-4">
          <StatBadge title="Rating" value={restaurant.rating || 0} />
          <StatBadge title="Cost" value={`$${restaurant.costRating}`} />
          <StatBadge title="Status" value={restaurant.status} />
        </div>
        <div className="text-sm text-slate-500">
          Hours: {restaurant.openingTime} - {restaurant.closingTime}
        </div>
        <div className="text-sm text-slate-500">
          Phone: {restaurant.phone}
        </div>
        <button
          className="mt-auto bg-blue-600 hover:bg-blue-700 text-white rounded-md px-4 py-2 font-medium"
          onClick={() => alert("Coming soon")}
        >
          Edit Restaurant
        </button>
      </div>
    </div>
  );
};

RestaurantCard.propTypes = {
  restaurant: PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    description: PropTypes.string,
    image: PropTypes.string,
    address: PropTypes.string,
    phone: PropTypes.string,
    cuisine: PropTypes.string,
    costRating: PropTypes.number,
    rating: PropTypes.number,
    status: PropTypes.string,
    openingTime: PropTypes.string,
    closingTime: PropTypes.string,
    approvalStatus: PropTypes.string,
  }).isRequired,
};

export default RestaurantCard; 