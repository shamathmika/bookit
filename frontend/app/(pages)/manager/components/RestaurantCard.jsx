"use client";
import React from "react";
import Image from "next/image";
import Link from "next/link";
import { MapPin, Clock, Star, Phone, CheckCircle, XCircle } from "lucide-react";

const RestaurantCard = ({ restaurant }) => {
  const {
    name,
    description,
    photos,
    address,
    contact,
    cuisine,
    costRating,
    avgStarRating,
    status,
    openingTime,
    closingTime,
    approvalStatus
  } = restaurant;

  return (
    <div className="bg-white rounded-xl shadow-md overflow-hidden">
      {/* Restaurant Image */}
      <div className="relative h-48">
        {photos && photos.length > 0 ? (
          <Image
            src={photos[0]}
            alt={name}
            fill
            className="object-cover"
          />
        ) : (
          <div className="w-full h-full bg-gray-200 flex items-center justify-center">
            <span className="text-gray-400">No image available</span>
          </div>
        )}
      </div>

      {/* Restaurant Info */}
      <div className="p-6">
        <div className="flex justify-between items-start mb-2">
          <h2 className="text-xl font-semibold">{name}</h2>
          <div className="flex items-center gap-1">
            <Star className="h-5 w-5 text-yellow-400" />
            <span>{avgStarRating.toFixed(1)}</span>
          </div>
        </div>

        <p className="text-gray-600 mb-4">{description}</p>

        <div className="space-y-2 mb-4">
          <div className="flex items-center text-gray-600">
            <MapPin className="h-4 w-4 mr-2" />
            <span>{address.fullAddress}</span>
          </div>
          <div className="flex items-center text-gray-600">
            <Phone className="h-4 w-4 mr-2" />
            <span>{contact}</span>
          </div>
          <div className="flex items-center text-gray-600">
            <Clock className="h-4 w-4 mr-2" />
            <span>{openingTime} - {closingTime}</span>
          </div>
        </div>

        <div className="flex justify-between items-center">
          <div className="flex items-center gap-2">
            <span className="text-sm text-gray-500">{cuisine}</span>
            <span className="text-sm text-gray-500">•</span>
            <span className="text-sm text-gray-500">{'$'.repeat(costRating)}</span>
          </div>
          <div className="flex items-center">
            {approvalStatus === 'APPROVED' ? (
              <CheckCircle className="h-5 w-5 text-green-500" />
            ) : approvalStatus === 'REJECTED' ? (
              <XCircle className="h-5 w-5 text-red-500" />
            ) : (
              <span className="text-yellow-500">Pending</span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default RestaurantCard; 