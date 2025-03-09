import { RestaurantCard } from "@/components/common/RestaurantCard";
import { Restaurants } from "@/constants/constants";

export default function Search() {
  return (
    <div className="flex min-h-screen mx-5">

      <div className="w-1/5 p-4 text-center bg-gray-100">

        Sidebar
      </div>


      {/* Main content */}
      <div className="w-4/5 p-4">
        <div className="mb-4">this is a search page</div>
        <div className="space-y-4">
        {Restaurants.map((restaurant) => (
              <RestaurantCard key={restaurant.id} {...restaurant} variant="search" />
            ))}
        </div>
      </div>


    </div>
  );
}
