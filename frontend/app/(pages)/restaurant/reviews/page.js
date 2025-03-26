"use client"

import { useState } from "react"
import { X, Camera, Plus } from "lucide-react"
import Image from "next/image"



export default function ReviewModal({ isOpen, onClose, restaurantName = "Restaurant" }) {
  const [rating, setRating] = useState(4)
  const [reviewText, setReviewText] = useState("")
  const [name, setName] = useState("")
  const [media, setMedia] = useState(["/placeholder.svg?height=80&width=80"])

  if (!isOpen) return null

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg w-full max-w-lg relative">
        <button onClick={onClose} className="absolute right-4 top-4 text-gray-500 hover:text-gray-700">
          <X className="h-5 w-5" />
        </button>

        <div className="p-6">
          <h2 className="text-xl font-bold mb-4">{restaurantName}</h2>

          <div className="flex items-center mb-6">
            {[1, 2, 3, 4, 5].map((star) => (
              <button key={star} onClick={() => setRating(star)} className="focus:outline-none">
                <StarIcon
                  className={`h-6 w-6 ${star <= rating ? "text-red-500 fill-current" : "text-red-500 fill-none stroke-current"}`}
                />
              </button>
            ))}
            <span className="text-sm text-gray-500 ml-2">Stars</span>
          </div>

          <div className="mb-4">
            <div className="flex items-center justify-between mb-1">
              <label htmlFor="name" className="text-base font-medium">
                Name
              </label>
              <button className="text-gray-500">
                <Camera className="h-5 w-5" />
              </button>
            </div>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border rounded-md p-2"
            />
          </div>

          <div className="mb-6">
            <textarea
              value={reviewText}
              onChange={(e) => setReviewText(e.target.value)}
              placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut eni"
              className="w-full border rounded-md p-2 h-24"
            />
          </div>

          <div className="mb-6">
            <label className="block text-base font-medium mb-2">Media</label>
            <div className="flex items-center gap-2">
              {media.map((src, index) => (
                <div key={index} className="border rounded-md w-20 h-20 overflow-hidden">
                  <Image
                    src={src || "/placeholder.svg"}
                    alt="Review media"
                    width={80}
                    height={80}
                    className="w-full h-full object-cover"
                  />
                </div>
              ))}
              <button className="border rounded-md w-20 h-20 flex items-center justify-center text-gray-400">
                <Plus className="h-8 w-8" />
              </button>
            </div>
          </div>

          <div className="flex justify-end">
            <button onClick={onClose} className="bg-[#8B2615] text-white px-6 py-2 rounded">
              Submit
            </button>
          </div>
        </div>
      </div>
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

