import { Link } from 'react-router-dom';
import { Card, CardContent } from '../../components/ui/card';
import { Button } from '../../components/ui/button';

// Mock data for restaurants
const restaurants = [
  {
    id: '1',
    name: 'The Italian Corner',
    address: '123 Main St, City Center',
    image: 'https://placehold.co/400x200?text=Restaurant+Image',
    tables: 12,
    availableTables: 8,
    occupiedTables: 4
  },
  {
    id: '2',
    name: 'Spice Paradise',
    address: '456 Oak St, Downtown',
    image: 'https://placehold.co/400x200?text=Restaurant+Image',
    tables: 8,
    availableTables: 3,
    occupiedTables: 5
  },
  {
    id: '3',
    name: 'Ocean Fresh',
    address: '789 Beach Rd, Seaside',
    image: 'https://placehold.co/400x200?text=Restaurant+Image',
    tables: 10,
    availableTables: 7,
    occupiedTables: 3
  }
];

const RestaurantManagerDashboard = () => {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">My Restaurants</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {restaurants.map((restaurant) => (
          <Card key={restaurant.id} className="overflow-hidden">
            <div className="h-48 bg-slate-700 flex items-center justify-center text-white">
              <span>Restaurant Image</span>
            </div>
            
            <CardContent className="p-6">
              <h2 className="text-xl font-bold">{restaurant.name}</h2>
              <p className="text-gray-500 text-sm">{restaurant.address}</p>
              
              <div className="grid grid-cols-3 gap-4 mt-4">
                <div className="bg-gray-100 p-3 text-center rounded-lg">
                  <p className="text-xs text-gray-500">Tables</p>
                  <p className="font-bold text-lg">{restaurant.tables}</p>
                </div>
                
                <div className="bg-gray-100 p-3 text-center rounded-lg">
                  <p className="text-xs text-gray-500">Available</p>
                  <p className="font-bold text-lg">{restaurant.availableTables}</p>
                </div>
                
                <div className="bg-gray-100 p-3 text-center rounded-lg">
                  <p className="text-xs text-gray-500">Occupied</p>
                  <p className="font-bold text-lg">{restaurant.occupiedTables}</p>
                </div>
              </div>
              
              <div className="mt-6">
                <Link to={/restaurant-manager/edit-restaurant/${restaurant.id}}>
                  <Button className="w-full bg-blue-500 hover:bg-blue-600">Edit Restaurant</Button>
                </Link>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default RestaurantManagerDashboard;