"use client";
import React from "react";
import PropTypes from "prop-types";

const timeSlots = [
  "11:00 AM - 1:00 PM",
  "1:00 PM - 3:00 PM",
  "3:00 PM - 5:00 PM",
  "5:00 PM - 7:00 PM",
  "7:00 PM - 9:00 PM",
];

const TableCard = ({ table, onToggle, isOccupiedSection }) => (
  <div
    className={`rounded-xl border p-5 flex flex-col gap-2 shadow bg-white ${
      isOccupiedSection
        ? "border-rose-200/25 bg-rose-50"
        : "border-green-200/25 bg-green-50"
    }`}
  >
    <div className="font-bold text-lg">Table #{table.number}</div>
    <div className="text-slate-600 text-sm">{table.seats} Seats</div>
    <div className="text-slate-600 text-sm">Time Slot:</div>
    <select
      className="border rounded px-2 py-1 text-sm"
      value={table.timeslot}
      disabled
    >
      {timeSlots.map((slot) => (
        <option key={slot} value={slot}>
          {slot}
        </option>
      ))}
    </select>
    <button
      className={`mt-3 rounded-md px-4 py-2 font-medium transition-colors ${
        isOccupiedSection
          ? "bg-rose-700 hover:bg-rose-800 text-white"
          : "bg-blue-600 hover:bg-blue-700 text-white"
      }`}
      onClick={onToggle}
    >
      {isOccupiedSection ? "Mark as Available" : "Mark as Occupied"}
    </button>
  </div>
);

TableCard.propTypes = {
  table: PropTypes.shape({
    id: PropTypes.string.isRequired,
    number: PropTypes.number.isRequired,
    seats: PropTypes.number.isRequired,
    timeslot: PropTypes.string.isRequired,
    occupied: PropTypes.bool.isRequired,
  }).isRequired,
  onToggle: PropTypes.func.isRequired,
  isOccupiedSection: PropTypes.bool.isRequired,
};

export default TableCard; 