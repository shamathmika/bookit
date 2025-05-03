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

const TableCard = ({ table }) => (
  <div
    className={`rounded-xl border p-5 flex flex-col gap-2 shadow bg-white ${
      table.status === "OCCUPIED"
        ? "border-rose-200/25 bg-rose-50"
        : "border-green-200/25 bg-green-50"
    }`}
  >
    <div className="font-bold text-lg">Table #{table.number}</div>
    <div className="text-slate-600 text-sm">{table.seats} Seats</div>
    <div className={`text-sm font-medium ${
      table.status === "OCCUPIED" ? "text-rose-700" : "text-green-700"
    }`}>
      {table.status}
    </div>
  </div>
);

TableCard.propTypes = {
  table: PropTypes.shape({
    id: PropTypes.string.isRequired,
    number: PropTypes.number.isRequired,
    seats: PropTypes.number.isRequired,
    status: PropTypes.string.isRequired,
  }).isRequired,
};

export default TableCard; 