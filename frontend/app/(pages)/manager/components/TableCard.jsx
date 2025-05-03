"use client";
import React from "react";
import { Users } from "lucide-react";
import PropTypes from "prop-types";

const timeSlots = [
  "11:00 AM - 1:00 PM",
  "1:00 PM - 3:00 PM",
  "3:00 PM - 5:00 PM",
  "5:00 PM - 7:00 PM",
  "7:00 PM - 9:00 PM",
];

const TableCard = ({ table }) => {
  const { tableNumber, capacity, status } = table;

  return (
    <div className="bg-white rounded-lg shadow-md p-4 border border-slate-200">
      <div className="flex justify-between items-center mb-2">
        <div className="flex items-center gap-2">
          <span className="text-lg font-semibold">Table {tableNumber}</span>
          <span className={`px-2 py-1 rounded-full text-xs font-medium ${
            status === 'AVAILABLE' 
              ? 'bg-green-100 text-green-800' 
              : 'bg-red-100 text-red-800'
          }`}>
            {status}
          </span>
        </div>
      </div>
      <div className="flex items-center gap-2 text-slate-600">
        <Users className="w-4 h-4" />
        <span>{capacity} seats</span>
      </div>
    </div>
  );
};

TableCard.propTypes = {
  table: PropTypes.shape({
    id: PropTypes.string.isRequired,
    number: PropTypes.number.isRequired,
    seats: PropTypes.number.isRequired,
    timeslot: PropTypes.string.isRequired,
    occupied: PropTypes.bool.isRequired,
  }).isRequired,
};

export default TableCard; 