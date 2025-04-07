"use client"
import Link from "next/link"

export default function AdminDashboard() {
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
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
            <div className="bg-gray-200 rounded-lg p-6">
              <h2 className="text-lg font-medium mb-4 text-center">Total reservations</h2>
              <p className="text-5xl font-bold text-center">5943</p>
            </div>

            <div className="bg-gray-200 rounded-lg p-6">
              <h2 className="text-lg font-medium mb-4 text-center">Restaurants</h2>
              <p className="text-5xl font-bold text-center">388</p>
            </div>

            <div className="bg-gray-200 rounded-lg p-6">
              <h2 className="text-lg font-medium mb-4 text-center">Pending approvals</h2>
              <p className="text-5xl font-bold text-center">15</p>
            </div>
          </div>

          <h2 className="text-2xl font-medium mb-6">graphs and charts</h2>

          {/* Charts */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="border rounded-lg p-6">
              <h3 className="text-lg font-medium mb-4">Monthly Reservations</h3>
              <div className="h-64">
                <MonthlyReservationsChart />
              </div>
            </div>

            <div className="border rounded-lg p-6">
              <h3 className="text-lg font-medium mb-4">Popular Time Slots</h3>
              <div className="h-64">
                <PopularTimeSlotsChart />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

function MonthlyReservationsChart() {
  // Simplified chart implementation
  const data = [250, 300, 290, 320, 400, 380]
  const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun"]

  return (
    <div className="w-full h-full flex flex-col">
      <div className="flex-1 relative">
        {/* Y-axis labels */}
        <div className="absolute left-0 top-0 bottom-0 flex flex-col justify-between text-xs">
          <span>600</span>
          <span>450</span>
          <span>300</span>
          <span>150</span>
          <span>0</span>
        </div>

        {/* Chart area - simplified representation */}
        <div className="ml-10 h-full flex items-end">
          <div className="w-full h-full flex items-end relative">
            {/* Line representation */}
            <div className="absolute top-1/4 left-0 right-0 border-t border-black"></div>
            <div className="absolute top-1/2 left-0 right-0 border-t border-black"></div>
            <div className="absolute top-3/4 left-0 right-0 border-t border-black"></div>

            {/* Line chart representation */}
            <div className="absolute inset-0 flex items-center">
              <div className="w-full h-1/2 border-2 border-black rounded-full"></div>
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
  )
}

function PopularTimeSlotsChart() {
  // Simplified chart implementation
  const timeSlots = ["11:00", "12:00", "13:00", "14:00", "17:00", "18:00", "19:00", "20:00", "21:00"]

  return (
    <div className="w-full h-full flex flex-col">
      <div className="flex-1 relative">
        {/* Y-axis labels */}
        <div className="absolute left-0 top-0 bottom-0 flex flex-col justify-between text-xs">
          <span>60</span>
          <span>45</span>
          <span>30</span>
          <span>15</span>
          <span>0</span>
        </div>

        {/* Chart area - simplified representation */}
        <div className="ml-10 h-full flex items-end">
          <div className="w-full h-full flex items-end">
            {/* Bar chart representation */}
            <div className="flex-1 flex justify-around items-end">
              <div className="w-8 h-[20%] bg-black"></div>
              <div className="w-8 h-[35%] bg-black"></div>
              <div className="w-8 h-[50%] bg-black"></div>
              <div className="w-8 h-[25%] bg-black"></div>
              <div className="w-8 h-[45%] bg-black"></div>
              <div className="w-8 h-[75%] bg-black"></div>
              <div className="w-8 h-[85%] bg-black"></div>
              <div className="w-8 h-[55%] bg-black"></div>
              <div className="w-8 h-[30%] bg-black"></div>
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

