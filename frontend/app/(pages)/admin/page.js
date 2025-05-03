"use client"
import Link from "next/link"
import { useEffect, useState } from "react"
import { useAuth } from "@/context/AuthContext"
import { useRouter } from "next/navigation"
import { 
  getAdminDashboardData,
  getPopularBookingSlots,
  getMonthlyBookingStats,
  getPendingRestaurants,
  approveRestaurant,
  rejectRestaurant
} from "@/constants/apis"

export default function AdminDashboard() {
  const { user, isLoggedIn } = useAuth()
  const router = useRouter()
  const [dashboardData, setDashboardData] = useState({
    totalRestaurants: 0,
    pendingApprovals: 0,
    totalBookingsLastMonth: 0
  })
  const [bookingStats, setBookingStats] = useState([])
  const [popularSlots, setPopularSlots] = useState({})
  const [monthlyStats, setMonthlyStats] = useState([])
  const [restaurants, setRestaurants] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!isLoggedIn || !user) {
      router.push('/login')
      return
    }

    const fetchDashboardData = async () => {
      try {
        // Fetch main dashboard data
        const dashboardData = await getAdminDashboardData()
        setDashboardData(dashboardData)

        // Fetch popular slots
        const popularSlotsData = await getPopularBookingSlots()
        setPopularSlots(popularSlotsData)

        // Fetch monthly stats
        const monthlyStatsData = await getMonthlyBookingStats()
        setMonthlyStats(monthlyStatsData)
      } catch (error) {
        console.error('Error fetching dashboard data:', error)
      }
    }

    fetchDashboardData()
  }, [isLoggedIn, user, router])

  useEffect(() => {
    if (!isLoggedIn || !user) {
      router.push('/login')
      return
    }

    const fetchRestaurants = async () => {
      try {
        const data = await getPendingRestaurants()
        setRestaurants(data)
      } catch (err) {
        console.error('Error fetching restaurants:', err)
        setError('Failed to load restaurants. Please try again later.')
      } finally {
        setLoading(false)
      }
    }

    fetchRestaurants()
  }, [user, isLoggedIn, router])

  const handleApprove = async (restaurantId) => {
    try {
      await approveRestaurant(restaurantId)
      // Refresh the list
      const updatedData = await getPendingRestaurants()
      setRestaurants(updatedData)
    } catch (err) {
      console.error('Error approving restaurant:', err)
      setError('Failed to approve restaurant. Please try again later.')
    }
  }

  const handleReject = async (restaurantId) => {
    try {
      await rejectRestaurant(restaurantId)
      // Refresh the list
      const updatedData = await getPendingRestaurants()
      setRestaurants(updatedData)
    } catch (err) {
      console.error('Error rejecting restaurant:', err)
      setError('Failed to reject restaurant. Please try again later.')
    }
  }

  if (!isLoggedIn) {
    return <div className="flex min-h-screen items-center justify-center">
      <p>Please sign in to access the admin dashboard</p>
    </div>
  }

  return (
    <div className="flex min-h-screen bg-white">
      {/* Sidebar */}
      <div className="w-64 bg-[#1a1a1a] text-white flex flex-col">
        <div className="p-6 border-b border-gray-700">
          <h1 className="text-xl font-medium">Admin</h1>
        </div>

        <nav className="flex-1">
          <Link href="/admin" className="flex items-center px-6 py-4 bg-[#b8a8a8] text-black">
            Dashboard
          </Link>
          <Link href="/admin/restaurants" className="flex items-center px-6 py-4 hover:bg-gray-800">
            Restaurants
          </Link>
        </nav>
      </div>

      {/* Main content */}
      <div className="flex-1 overflow-auto">
        <div className="p-8">
          <h1 className="text-3xl font-bold mb-8">DASHBOARD</h1>

          {/* Stats cards */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-12">
            <div className="bg-gray-200 rounded-lg p-6">
              <h2 className="text-lg font-medium mb-4 text-center">Total Restaurants</h2>
              <p className="text-5xl font-bold text-center">{dashboardData.totalRestaurants}</p>
            </div>

            <div className="bg-gray-200 rounded-lg p-6">
              <h2 className="text-lg font-medium mb-4 text-center">Pending Approvals</h2>
              <p className="text-5xl font-bold text-center">{dashboardData.pendingApprovals}</p>
            </div>

            <div className="bg-gray-200 rounded-lg p-6">
              <h2 className="text-lg font-medium mb-4 text-center">Total Bookings</h2>
              <p className="text-5xl font-bold text-center">{monthlyStats[0]?.totalBookings || 0}</p>
            </div>

            <div className="bg-gray-200 rounded-lg p-6">
              <h2 className="text-lg font-medium mb-4 text-center">Cancellations</h2>
              <p className="text-5xl font-bold text-center">{monthlyStats[0]?.totalCancellations || 0}</p>
            </div>
          </div>

          {/* Restaurant-wise Stats */}
          <div className="mb-12">
            <h2 className="text-2xl font-medium mb-6">Restaurant Performance</h2>
            <div className="bg-white rounded-lg shadow overflow-hidden">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Restaurant ID</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Bookings</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cancellations</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Success Rate</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {bookingStats.map((stat) => (
                    <tr key={stat.id}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{stat.restaurantID}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{stat.totalBookings}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{stat.totalCancellations}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {stat.totalBookings > 0 
                          ? `${Math.round(((stat.totalBookings - stat.totalCancellations) / stat.totalBookings) * 100)}%`
                          : 'N/A'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Charts Section */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="border rounded-lg p-6">
              <h3 className="text-lg font-medium mb-4">Monthly Reservations</h3>
              <div className="h-64">
                <MonthlyReservationsChart data={monthlyStats} />
              </div>
            </div>

            <div className="border rounded-lg p-6">
              <h3 className="text-lg font-medium mb-4">Popular Time Slots</h3>
              <div className="h-64">
                <PopularTimeSlotsChart data={popularSlots} />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

function MonthlyReservationsChart({ data }) {
  // Ensure data is an array, default to empty array if not
  const safeData = Array.isArray(data) ? data : [];
  
  const months = safeData.map(stat => {
    if (!stat?._id) return '';
    return stat._id.split('-')[1];
  });
  
  const bookings = safeData.map(stat => stat?.totalBookings || 0);
  const cancellations = safeData.map(stat => stat?.totalCancellations || 0);

  // If no data, show a message
  if (safeData.length === 0) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        <p className="text-gray-500">No data available</p>
      </div>
    );
  }

  const maxBookings = Math.max(...bookings, 1); // Ensure at least 1 to avoid division by zero

  return (
    <div className="w-full h-full flex flex-col">
      <div className="flex-1 relative">
        {/* Y-axis labels */}
        <div className="absolute left-0 top-0 bottom-0 flex flex-col justify-between text-xs">
          <span>{maxBookings}</span>
          <span>{Math.round(maxBookings * 0.75)}</span>
          <span>{Math.round(maxBookings * 0.5)}</span>
          <span>{Math.round(maxBookings * 0.25)}</span>
          <span>0</span>
        </div>

        {/* Chart area */}
        <div className="ml-10 h-full flex items-end">
          <div className="w-full h-full flex items-end relative">
            {/* Grid lines */}
            <div className="absolute top-1/4 left-0 right-0 border-t border-gray-200"></div>
            <div className="absolute top-1/2 left-0 right-0 border-t border-gray-200"></div>
            <div className="absolute top-3/4 left-0 right-0 border-t border-gray-200"></div>

            {/* Bars */}
            <div className="flex-1 flex justify-around items-end">
              {bookings.map((value, index) => (
                <div key={index} className="flex flex-col items-center">
                  <div 
                    className="w-8 bg-blue-500" 
                    style={{ height: `${(value / maxBookings) * 100}%` }}
                  ></div>
                  <div 
                    className="w-8 bg-red-500 mt-1" 
                    style={{ height: `${(cancellations[index] / maxBookings) * 100}%` }}
                  ></div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* X-axis labels */}
      <div className="flex justify-between px-8 mt-2">
        {months.map((month, index) => (
          <div key={index} className="text-xs">
            {month}
          </div>
        ))}
      </div>
    </div>
  );
}

function PopularTimeSlotsChart({ data }) {
  const timeSlots = Object.keys(data)
  const values = Object.values(data)
  const maxValue = Math.max(...values)

  return (
    <div className="w-full h-full flex flex-col">
      <div className="flex-1 relative">
        {/* Y-axis labels */}
        <div className="absolute left-0 top-0 bottom-0 flex flex-col justify-between text-xs">
          <span>{maxValue}</span>
          <span>{Math.round(maxValue * 0.75)}</span>
          <span>{Math.round(maxValue * 0.5)}</span>
          <span>{Math.round(maxValue * 0.25)}</span>
          <span>0</span>
        </div>

        {/* Chart area */}
        <div className="ml-10 h-full flex items-end">
          <div className="w-full h-full flex items-end">
            {/* Bar chart */}
            <div className="flex-1 flex justify-around items-end">
              {values.map((value, index) => (
                <div
                  key={index}
                  className="w-8 bg-green-500"
                  style={{ height: `${(value / maxValue) * 100}%` }}
                ></div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* X-axis labels */}
      <div className="flex justify-between px-4 mt-2">
        {timeSlots.map((time, index) => (
          <div key={index} className="text-xs">
            {time}
          </div>
        ))}
      </div>
    </div>
  )
}

