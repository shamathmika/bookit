"use client"

import Link from "next/link"
import Image from "next/image"
import { Github, Twitter } from "lucide-react"

export default function UserReviews() {
  const reviews = [
    {
      id: 1,
      restaurant: "Restaurant",
      rating: 4,
      date: "Date",
      text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut eni",
      image: "/placeholder.svg?height=60&width=60",
    },
  ]

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
              <Link href="/user/reservations" className="block py-2 text-gray-400 hover:text-gray-600">
                My Reservations
              </Link>
            </li>
            <li>
              <Link href="/user/reviews" className="block py-2 text-[#8B2615] font-medium">
                My Reviews
              </Link>
            </li>
          </ul>
        </nav>
      </div>

      {/* Main content */}
      <div className="flex-1 p-8">
        <h1 className="text-3xl font-bold mb-8">My Reviews</h1>

        <div className="space-y-6">
          {reviews.map((review) => (
            <div key={review.id} className="border rounded-lg p-6 max-w-3xl">
              <div className="flex">
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
                  <div className="flex justify-between items-start">
                    <h2 className="text-xl font-bold">{review.restaurant}</h2>
                    <span className="text-gray-500">{review.date}</span>
                  </div>

                  <div className="flex text-red-500 my-2">
                    {[1, 2, 3, 4, 5].map((star) => (
                      <StarIcon key={star} filled={star <= review.rating} className="h-5 w-5" />
                    ))}
                  </div>

                  <p className="text-gray-700 mb-4">{review.text}</p>

                  {review.image && (
                    <div className="inline-block border rounded-md overflow-hidden">
                      <Image
                        src={review.image || "/placeholder.svg"}
                        alt="Review image"
                        width={60}
                        height={60}
                        className="object-cover"
                      />
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

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

function StarIcon({ filled, className }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill={filled ? "currentColor" : "none"}
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      className={className}
    >
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  )
}
