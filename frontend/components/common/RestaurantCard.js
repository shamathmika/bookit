import{ StarIcon} from "@/components/common/StarIcon";


import Image from "next/image";

export  function RestaurantCard(
  {
    name,
    image,
    rating,
    cuisine,
    priceRange,
    region,
    bookings,
    time,
  }
) {
  return (
    <div className="min-w-[250px] border rounded-md overflow-hidden">
      <div className="relative">
        <Image
          src="https://plus.unsplash.com/premium_photo-1675344317686-118cc9f89f8a?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt="Restaurant table"
          width={250}
          height={150}
          className="w-full"
        />
      </div>

      <div className="p-3">
        <h3 className="font-medium">{name}</h3>
        <div className="flex items-center text-sm">
          <div className="flex text-yellow-400">
            {[1, 2, 3, 4, 5].map((star) => (
              <StarIcon key={star} className="h-4 w-4" />
            ))}
          </div>
          <span className="text-xs text-gray-500 ml-1">(Round down review #)</span>
        </div>

        <div className="text-xs text-gray-500">{cuisine} | {priceRange} | {region}</div>

        <div className="text-xs mt-2">Booked {bookings} times today</div>

        <div className="flex gap-1 mt-2">
          <button className="bg-[#f8f5f0] text-xs px-2 py-1 rounded">{time-30}</button>
          <button className="bg-[#f8f5f0] text-xs px-2 py-1 rounded">{time}</button>
          <button className="bg-[#f8f5f0] text-xs px-2 py-1 rounded"> {time+30} </button>
        </div>
      </div>
    </div>
  )
}