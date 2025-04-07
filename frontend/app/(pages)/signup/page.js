"use client"

import { useState } from "react"
import Link from "next/link"
import { Eye, EyeOff, Github, Twitter } from "lucide-react"

export default function Signup() {
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <div className="flex-1 flex flex-col items-center justify-center px-4 py-12">
        <div className="w-full max-w-md">
          {/* Tabs */}
          <div className="flex justify-center text-3xl font-bold mb-12">
            <Link href="/login" className="text-gray-300 hover:text-gray-400">
              Sign In
            </Link>
            <span className="mx-4 text-gray-300">|</span>
            <h1 className="text-[#8B2615]">Sign Up</h1>
          </div>

          <form className="space-y-6">
            {/* Name input */}
            <div className="relative">
              <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
              <input type="text" className="w-full px-4 py-3 border rounded-md outline-none" />
            </div>

            {/* Phone input */}
            <div className="relative">
              <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
              <input type="tel" className="w-full px-4 py-3 bg-gray-100 rounded-md outline-none" />
            </div>

            {/* Email input */}
            <div className="relative">
              <label className="block text-sm font-medium text-gray-700 mb-1">Email Address</label>
              <input type="email" className="w-full px-4 py-3 border border-blue-400 rounded-md outline-none" />
            </div>

            {/* Password input */}
            <div className="relative">
              <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
              <div className="flex items-center bg-gray-100 rounded-md">
                <input
                  type={showPassword ? "text" : "password"}
                  className="w-full px-4 py-3 bg-transparent outline-none"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 text-gray-400"
                >
                  {showPassword ? <Eye className="h-5 w-5" /> : <EyeOff className="h-5 w-5" />}
                </button>
              </div>
            </div>

            {/* Confirm Password input */}
            <div className="relative">
              <label className="block text-sm font-medium text-gray-700 mb-1">Confirm Password</label>
              <div className="flex items-center bg-gray-100 rounded-md">
                <input
                  type={showConfirmPassword ? "text" : "password"}
                  className="w-full px-4 py-3 bg-transparent outline-none"
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  className="absolute right-3 text-gray-400"
                >
                  {showConfirmPassword ? <Eye className="h-5 w-5" /> : <EyeOff className="h-5 w-5" />}
                </button>
              </div>
            </div>

            {/* Sign Up button */}
            <button type="submit" className="w-full bg-[#8B2615] text-white py-3 rounded-md font-medium">
              Sign Up
            </button>

            {/* Divider */}
            <div className="flex items-center">
              <div className="flex-1 border-t border-gray-300"></div>
              <span className="px-4 text-gray-500">or</span>
              <div className="flex-1 border-t border-gray-300"></div>
            </div>

            {/* Google sign in */}
            <button
              type="button"
              className="w-full flex items-center justify-center gap-2 border border-gray-300 py-3 rounded-md font-medium"
            >
              <GoogleIcon className="h-5 w-5" />
              <span>Sign In with Google</span>
            </button>
          </form>
        </div>
      </div>

      <footer className="text-center py-4 text-sm text-gray-600 border-t">
        <div>(C) 2025 Maverick, Inc</div>
        <div className="flex justify-center gap-4 mt-2">
          <Github size={16} />
          <Twitter size={16} />
        </div>
      </footer>
    </div>
  )
}

function GoogleIcon({ className }) {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className={className}>
      <path
        fill="#EA4335"
        d="M5.26620003,9.76452941 C6.19878754,6.93863203 8.85444915,4.90909091 12,4.90909091 C13.6909091,4.90909091 15.2181818,5.50909091 16.4181818,6.49090909 L19.9090909,3 C17.7818182,1.14545455 15.0545455,0 12,0 C7.27006974,0 3.1977497,2.69829785 1.23999023,6.65002441 L5.26620003,9.76452941 Z"
      />
      <path
        fill="#34A853"
        d="M16.0407269,18.0125889 C14.9509167,18.7163016 13.5660892,19.0909091 12,19.0909091 C8.86648613,19.0909091 6.21911939,17.076871 5.27698177,14.2678769 L1.23746264,17.3349879 C3.19279051,21.2970142 7.26500293,24 12,24 C14.9328362,24 17.7353462,22.9573905 19.834192,20.9995801 L16.0407269,18.0125889 Z"
      />
      <path
        fill="#4A90E2"
        d="M19.834192,20.9995801 C22.0291676,18.9520994 23.4545455,15.903663 23.4545455,12 C23.4545455,11.2909091 23.3454545,10.5818182 23.1272727,9.90909091 L12,9.90909091 L12,14.4545455 L18.4363636,14.4545455 C18.1187732,16.013626 17.2662994,17.2212117 16.0407269,18.0125889 L19.834192,20.9995801 Z"
      />
      <path
        fill="#FBBC05"
        d="M5.27698177,14.2678769 C5.03832634,13.556323 4.90909091,12.7937589 4.90909091,12 C4.90909091,11.2182781 5.03443647,10.4668121 5.26620003,9.76452941 L1.23999023,6.65002441 C0.43658717,8.26043162 0,10.0753848 0,12 C0,13.9195484 0.444780743,15.7301709 1.23746264,17.3349879 L5.27698177,14.2678769 Z"
      />
    </svg>
  )
}

