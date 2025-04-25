"use client"

import { useState, useEffect } from "react"
import { Search, User, Calendar, Clock, MapPin, Mail, Phone, ChevronDown, Github, Twitter } from "lucide-react"
import Image from "next/image"
import ReviewModal from "../reviews/page"
import { useParams } from "next/navigation"

export default function RestaurantDetails() {
  const [isReviewModalOpen, setIsReviewModalOpen] = useState(false)
  const [restaurant, setRestaurant] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const params = useParams()

  useEffect(() => {
    const fetchRestaurantDetails = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/restaurants/${params.id}`)
        if (!response.ok) {
          throw new Error('Failed to fetch restaurant details')
        }
        const data = await response.json()
        setRestaurant(data)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchRestaurantDetails()
  }, [params.id])

  if (loading) {
    return (
      <div className="flex flex-col min-h-screen bg-white">
        <main className="flex-1 container mx-auto px-4 py-6">
          <div className="animate-pulse">
            <div className="h-8 bg-gray-200 rounded w-1/3 mb-6"></div>
            <div className="h-64 bg-gray-200 rounded mb-6"></div>
            <div className="grid md:grid-cols-3 gap-6">
              <div className="md:col-span-2">
                <div className="h-4 bg-gray-200 rounded w-1/2 mb-4"></div>
                <div className="h-4 bg-gray-200 rounded w-3/4 mb-4"></div>
              </div>
              <div className="md:col-span-1">
                <div className="h-48 bg-gray-200 rounded"></div>
              </div>
            </div>
          </div>
        </main>
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex flex-col min-h-screen bg-white">
        <main className="flex-1 container mx-auto px-4 py-6">
          <div className="text-red-500">Error: {error}</div>
        </main>
      </div>
    )
  }

  if (!restaurant) {
    return (
      <div className="flex flex-col min-h-screen bg-white">
        <main className="flex-1 container mx-auto px-4 py-6">
          <div>Restaurant not found</div>
        </main>
      </div>
    )
  }

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <main className="flex-1 container mx-auto px-4 py-0">
        <div className="w-full h-64 md:h-80 bg-gray-100 overflow-hidden">
          <Image
              src="https://plus.unsplash.com/premium_photo-1675344317686-118cc9f89f8a?q=80&w=2940&auto=format&fit=crop"
              alt="Restaurant banner"
            width={1000}
            height={320}
            className="w-full object-cover"
          />
        </div>

        <div className="grid md:grid-cols-3 gap-6 mt-6">
          <div className="md:col-span-2">
            <h1 className="text-2xl font-bold">{restaurant.name}</h1>

            <div className="flex flex-wrap items-center gap-4 mt-2">
              <div className="flex items-center">
                <div className="flex text-red-500">
                  {[...Array(5)].map((_, i) => (
                    <StarIcon 
                      key={i} 
                      className={`h-5 w-5 ${i < Math.floor(restaurant.avgRating) ? 'fill-current' : 'stroke-current fill-none'}`} 
                    />
                  ))}
                </div>
                <span className="text-xs text-gray-500 ml-1">({restaurant.avgRating.toFixed(1)})</span>
              </div>

              <div className="flex items-center gap-1 text-sm">
                <CircleIcon className="h-4 w-4" />
                <span>{restaurant.totalReviews} reviews</span>
              </div>

              <div className="flex items-center gap-1 text-sm">
                <DollarIcon className="h-4 w-4" />
                <span>{restaurant.costRating === 1 ? '$' : restaurant.costRating === 2 ? '$$' : '$$$'}</span>
              </div>

              <div className="flex items-center gap-1 text-sm">
                <ForkKnifeIcon className="h-4 w-4" />
                <span>{restaurant.cuisine}</span>
              </div>
            </div>

            <div className="text-sm mt-2">Booked {restaurant.bookedToday} times today</div>

            <div className="mt-6 border-t pt-4">
              <h2 className="text-lg font-medium text-red-500">About</h2>
              <p className="mt-2 text-sm">
                {restaurant.description}
              </p>
            </div>

            <div className="mt-4 grid gap-2">
              <div className="flex items-center gap-2 text-sm">
                <Phone className="h-4 w-4" />
                <span className="font-medium">Call:</span>
                <span>{restaurant.contact}</span>
              </div>
            </div>

            <div className="mt-6 border-t pt-4">
              <div className="flex justify-between items-center">
                <h2 className="text-lg font-medium text-red-500">Reviews</h2>
                <button
                  className="bg-red-500 text-white px-4 py-1 rounded text-sm"
                  onClick={() => setIsReviewModalOpen(true)}
                >
                  Write a Review
                </button>
              </div>

              {restaurant.reviews.map((review) => (
                <div key={review.id} className="mt-4 border-b pb-4">
                  <div className="flex justify-between">
                    <h3 className="font-medium">Anonymous User</h3>
                    <span className="text-sm text-gray-500">
                      {new Date(review.date).toLocaleDateString()}
                    </span>
                  </div>

                  <div className="flex text-red-500 mt-1">
                    {[...Array(5)].map((_, i) => (
                      <StarIcon 
                        key={i} 
                        className={`h-4 w-4 ${i < review.rating ? 'fill-current' : 'stroke-current fill-none'}`} 
                      />
                    ))}
                  </div>

                  {review.comments && (
                    <p className="mt-2 text-sm">{review.comments}</p>
                  )}

                  {review.photos && review.photos.length > 0 && (
                    <div className="flex gap-2 mt-2">
                      {review.photos.map((photo, index) => (
                        <div key={index} className="w-20 h-20 rounded overflow-hidden">
                          <Image
                            src={photo}
                            alt={`Review photo ${index + 1}`}
                            width={80}
                            height={80}
                            className="w-full h-full object-cover"
                          />
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>

          <div className="md:col-span-1">
            <div className="border rounded-md p-4">
              <h2 className="text-xl font-medium text-red-500 text-center mb-4">Reserve a Table</h2>

              <div className="grid gap-4">
                <div className="relative">
                  <div className="flex items-center border rounded overflow-hidden">
                    <User className="ml-2 h-4 w-4 text-gray-500" />
                    <span className="px-2"># People</span>
                    <ChevronDown className="ml-auto mr-2 h-4 w-4" />
                  </div>
                </div>

                <div className="relative">
                  <div className="flex items-center border rounded overflow-hidden">
                    <Calendar className="ml-2 h-4 w-4 text-gray-500" />
                    <span className="px-2">Date</span>
                    <ChevronDown className="ml-auto mr-2 h-4 w-4" />
                  </div>
                </div>

                <div className="relative">
                  <div className="flex items-center border rounded overflow-hidden">
                    <Clock className="ml-2 h-4 w-4 text-gray-500" />
                    <span className="px-2">Time</span>
                    <ChevronDown className="ml-auto mr-2 h-4 w-4" />
                  </div>
                </div>
              </div>
            </div>

            <div className="border rounded-md p-4 mt-6">
              <div className="flex items-center gap-2 text-red-500 mb-4">
                <MapPin className="h-4 w-4" />
                <h3 className="font-medium">Address Details</h3>
              </div>

              <div className="text-sm mb-4">
                <p>{restaurant.street}</p>
                <p>{restaurant.city}, {restaurant.state} {restaurant.zipCode}</p>
              </div>

              <div className="bg-gray-100 rounded-md overflow-hidden h-48 relative">
                <iframe
                  src={restaurant.googleMapsEmbedUrl}
                  width="100%"
                  height="100%"
                  style={{ border: 0 }}
                  allowFullScreen=""
                  loading="lazy"
                  referrerPolicy="no-referrer-when-downgrade"
                ></iframe>
              </div>
            </div>
          </div>
        </div>
      </main>

      <footer className="text-center py-4 text-sm text-gray-600 border-t mt-8">
        <div>(C) 2025 Maverick, Inc</div>
        <div className="flex justify-center gap-4 mt-2">
          <Github size={16} />
          <Twitter size={16} />
        </div>
      </footer>

      <ReviewModal 
        isOpen={isReviewModalOpen} 
        onClose={() => setIsReviewModalOpen(false)} 
        restaurantName={restaurant.name} 
      />
    </div>
  )
}

function StarIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  )
}

function CircleIcon(props) {
  return (
    <svg
      {...props}
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
      <circle cx="12" cy="12" r="10" />
    </svg>
  )
}

function DollarIcon(props) {
  return (
    <svg
      {...props}
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
      <line x1="12" y1="2" x2="12" y2="22" />
      <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
    </svg>
  )
}

function ForkKnifeIcon(props) {
  return (
    <svg
      {...props}
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
      <path d="M3 2v7c0 1.1.9 2 2 2h4a2 2 0 0 0 2-2V2" />
      <path d="M7 2v20" />
      <path d="M21 15V2v0a5 5 0 0 0-5 5v6c0 1.1.9 2 2 2h3Zm0 0v7" />
    </svg>
  )
}

