"use client";
import React from "react";
import PropTypes from "prop-types";
import StatBadge from "./StatBadge";

const RestaurantCard = ({ restaurant }) => {
  const total = restaurant.tables.length;
  const occupied = restaurant.tables.filter((t) => t.occupied).length;
  const available = total - occupied;

  return (
    <div className="bg-white rounded-xl shadow flex flex-col overflow-hidden">
      <div className="bg-slate-700 text-white flex items-center justify-center h-36 rounded-t-xl text-lg font-semibold">
        Restaurant Image
      </div>
      <div className="p-5 flex-1 flex flex-col gap-2">
        <div className="font-bold text-lg">{restaurant.name}</div>
        <div className="text-slate-500 text-sm mb-2">{restaurant.address}</div>
        <div className="flex gap-2 mb-4">
          <StatBadge title="Tables" value={total} />
          <StatBadge title="Available" value={available} />
          <StatBadge title="Occupied" value={occupied} />
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
    name: PropTypes.string.isRequired,
    address: PropTypes.string,
    tables: PropTypes.arrayOf(
      PropTypes.shape({
        occupied: PropTypes.bool,
      })
    ),
  }).isRequired,
};

export default RestaurantCard; 