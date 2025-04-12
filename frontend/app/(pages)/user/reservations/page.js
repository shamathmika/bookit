"use client"

import { useState } from "react"
import Link from "next/link"
import Image from "next/image"
import { User, Calendar, Clock, Github, Twitter } from "lucide-react"

export default function UserReservations() {
  const [reservations, setReservations] = useState([
    {
      id: 1,
      restaurant: "Restaurant",
      people: "2",
      date: "Full Date",
      time: "Time",
      status: "active",
    },
    {
      id: 2,
      restaurant: "Restaurant",
      people: "4",
      date: "Full Date",
      time: "Time",
      status: "cancelled",
    },
  ])

  const [showCancelModal, setShowCancelModal] = useState(false)
  const [showCancelledModal, setShowCancelledModal] = useState(false)
  const [selectedReservation, setSelectedReservation] = useState(null)

  const handleCancelClick = (reservation) => {
    setSelectedReservation(reservation)
    setShowCancelModal(true)
  }

  const handleCancelConfirm = () => {
    // Update the reservation status to cancelled
    setReservations(
      reservations.map((res) => (res.id === selectedReservation.id ? { ...res, status: "cancelled" } : res)),
    )
    setShowCancelModal(false)
    setShowCancelledModal(true)
  }

  return (
    <div className="flex min-h-screen bg-white">
      {/* Sidebar */}
      <div className="w-64 border-r">
        <nav className="py-8 px-6">
          <ul className="space-y-2">
            <li>
              <Link href="/user" className="block py-2 text-gray-400 hover:text-gray-600">
                My Profile
              </Link>
            </li>
            <li>
              <Link href="/user/reservations" className="block py-2 text-[#8B2615] font-medium">
                My Reservations
              </Link>
            </li>
            <li>
              <Link href="/user/reviews" className="block py-2 text-gray-400 hover:text-gray-600">
                My Reviews
              </Link>
            </li>
          </ul>
        </nav>
      </div>

      {/* Main content */}
      <div className="flex-1 p-8">
        <h1 className="text-3xl font-bold mb-8">My Reservations</h1>

        <div className="space-y-6">
          {reservations.map((reservation) => (
            <div key={reservation.id} className="border rounded-lg p-6 max-w-3xl">
              <div className="flex items-center">
                <div className="flex-shrink-0 mr-4">
                  <Image
                    src="/placeholder.svg?height=100&width=100"
                    alt="Restaurant"
                    width={100}
                    height={100}
                    className="rounded-md"
                  />
                </div>

                <div className="flex-1">
                  <h2 className="text-xl font-bold mb-2">{reservation.restaurant}</h2>
                  <div className="flex flex-wrap gap-4 mb-2">
                    <div className="flex items-center">
                      <User className="h-5 w-5 mr-1" />
                      <span># {reservation.people}</span>
                    </div>
                    <div className="flex items-center">
                      <Calendar className="h-5 w-5 mr-1" />
                      <span>{reservation.date}</span>
                    </div>
                    <div className="flex items-center">
                      <Clock className="h-5 w-5 mr-1" />
                      <span>{reservation.time}</span>
                    </div>
                  </div>
                </div>

                <div className="ml-auto">
                  {reservation.status === "active" ? (
                    <button
                      onClick={() => handleCancelClick(reservation)}
                      className="border border-[#8B2615] text-[#8B2615] px-4 py-2 rounded-md"
                    >
                      Cancel
                    </button>
                  ) : (
                    <div className="border px-4 py-2 rounded-md text-gray-500">Cancelled</div>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Cancellation Confirmation Modal */}
      {showCancelModal && selectedReservation && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md w-full">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-2xl font-bold text-[#8B2615]">Ask for Cancellation Confirmation</h2>
              <button onClick={() => setShowCancelModal(false)} className="text-gray-500">
                <XIcon />
              </button>
            </div>

            <div className="flex items-center mb-6">
              <div className="flex-shrink-0 mr-4">
                <Image
                  src="/placeholder.svg?height=80&width=80"
                  alt="Restaurant"
                  width={80}
                  height={80}
                  className="rounded-md"
                />
              </div>
              <div>
                <h3 className="text-xl font-bold">{selectedReservation.restaurant}</h3>
                <div className="flex flex-wrap gap-4 mt-2">
                  <div className="flex items-center">
                    <User className="h-4 w-4 mr-1" />
                    <span># {selectedReservation.people}</span>
                  </div>
                  <div className="flex items-center">
                    <Calendar className="h-4 w-4 mr-1" />
                    <span>{selectedReservation.date}</span>
                  </div>
                  <div className="flex items-center">
                    <Clock className="h-4 w-4 mr-1" />
                    <span>{selectedReservation.time}</span>
                  </div>
                </div>
              </div>
            </div>

            <div className="flex justify-end gap-4">
              <button onClick={() => setShowCancelModal(false)} className="text-[#8B2615] font-medium">
                No
              </button>
              <button
                onClick={handleCancelConfirm}
                className="bg-[#8B2615] text-white px-6 py-2 rounded-md font-medium"
              >
                Yes
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Cancellation Success Modal */}
      {showCancelledModal && selectedReservation && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md w-full">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-2xl font-bold text-[#8B2615] flex items-center">
                <CheckIcon className="mr-2" /> Confirm Cancelled Message
              </h2>
              <button onClick={() => setShowCancelledModal(false)} className="text-gray-500">
                <XIcon />
              </button>
            </div>

            <div className="flex items-center mb-6">
              <div className="flex-shrink-0 mr-4">
                <Image
                  src="/placeholder.svg?height=80&width=80"
                  alt="Restaurant"
                  width={80}
                  height={80}
                  className="rounded-md"
                />
              </div>
              <div>
                <h3 className="text-xl font-bold">{selectedReservation.restaurant}</h3>
                <div className="flex flex-wrap gap-4 mt-2">
                  <div className="flex items-center">
                    <User className="h-4 w-4 mr-1" />
                    <span># {selectedReservation.people}</span>
                  </div>
                  <div className="flex items-center">
                    <Calendar className="h-4 w-4 mr-1" />
                    <span>{selectedReservation.date}</span>
                  </div>
                  <div className="flex items-center">
                    <Clock className="h-4 w-4 mr-1" />
                    <span>{selectedReservation.time}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      <footer className="text-center py-4 text-sm text-gray-600 border-t absolute bottom-0 w-full">
        <div>(C) 2025 Maverick, Inc</div>
        <div className="flex justify-center gap-4 mt-2">
          <Github size={16} />
          <Twitter size={16} />
        </div>
      </footer>
    </div>
  )
}

function XIcon() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <line x1="18" y1="6" x2="6" y2="18"></line>
      <line x1="6" y1="6" x2="18" y2="18"></line>
    </svg>
  )
}

function CheckIcon({ className }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      className={className}
    >
      <polyline points="20 6 9 17 4 12"></polyline>
    </svg>
  )
}
