import {StarIcon} from "@/components/common/StarIcon";


import Image from "next/image";

export  function RestaurantCard() {
  return (
    <div className="min-w-[250px] border rounded-md overflow-hidden">
      <div className="relative">
        <Image
          src="/placeholder.svg?height=150&width=250"
          alt="Restaurant table"
          width={250}
          height={150}
          className="w-full"
        />
      </div>

      <div className="p-3">
        <h3 className="font-medium">Restaurant</h3>
        <div className="flex items-center text-sm">
          <div className="flex text-yellow-400">
            {[1, 2, 3, 4, 5].map((star) => (
              <StarIcon key={star} className="h-4 w-4" />
            ))}
          </div>
          <span className="text-xs text-gray-500 ml-1">(Round down review #)</span>
        </div>

        <div className="text-xs text-gray-500">Cuisine | $$ | Region</div>

        <div className="text-xs mt-2">Booked 8 times today</div>

        <div className="flex gap-1 mt-2">
          <button className="bg-[#f8f5f0] text-xs px-2 py-1 rounded">Time - 30</button>
          <button className="bg-[#f8f5f0] text-xs px-2 py-1 rounded">Time</button>
          <button className="bg-[#f8f5f0] text-xs px-2 py-1 rounded">Time + 30</button>
        </div>
      </div>
    </div>
  )
}