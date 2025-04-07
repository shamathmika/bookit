"use client"

import { useState } from "react"
import Link from "next/link"
import { Eye, EyeOff, Mail, Phone, Github, Twitter } from "lucide-react"
import { useRouter } from "next/navigation"

export default function Login() {
  const [showPassword, setShowPassword] = useState(false)
  const [contactMethod, setContactMethod] = useState("phone")
  const [contact, setContact] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState("")
  const router=useRouter()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")

    try {
      const response = await fetch("http://localhost:8080/api/auth/signin", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: contact, // backend only expects email (even if you show phone UI)
          password,
        }),
      })

      if (!response.ok) {
        const data = await response.json()
        setError(data?.message || "Login failed. Please try again.")
        return
      }

      const data = await response.json()
      console.log("Login success:", data)

      // ✅ Save token and role
      localStorage.setItem("token", data.token)
      localStorage.setItem("role", data.role)
  
      // ✅ Redirect
      router.push("/home")

    } catch (err) {
      setError("Network error. Please try again later.")
    }
  }

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <div className="flex-1 flex flex-col items-center justify-center px-4 py-12">
        <div className="w-full max-w-md">
          <div className="flex justify-center text-3xl font-bold mb-12">
            <h1 className="text-[#8B2615]">Sign In</h1>
            <span className="mx-4 text-gray-300">|</span>
            <Link href="/signup" className="text-gray-300 hover:text-gray-400">
              Sign Up
            </Link>
          </div>

          <form className="space-y-6" onSubmit={handleSubmit}>
            <div className="relative">
              <div className="flex border rounded-md overflow-hidden">
                <div className="flex">
                  <button
                    type="button"
                    className={`flex items-center justify-center w-12 h-12 ${contactMethod === "phone" ? "bg-gray-100" : "bg-white"}`}
                    onClick={() => setContactMethod("phone")}
                  >
                    <Phone className="h-5 w-5 text-gray-500" />
                  </button>
                  <button
                    type="button"
                    className={`flex items-center justify-center w-12 h-12 ${contactMethod === "email" ? "bg-gray-100" : "bg-white"}`}
                    onClick={() => setContactMethod("email")}
                  >
                    <Mail className="h-5 w-5 text-gray-500" />
                  </button>
                </div>
                <input
                  type={contactMethod === "email" ? "email" : "tel"}
                  placeholder={contactMethod === "email" ? "Email" : "Phone Number"}
                  className="flex-1 px-4 py-3 outline-none"
                  value={contact}
                  onChange={(e) => setContact(e.target.value)}
                />
              </div>
            </div>

            <div className="relative">
              <div className="flex items-center bg-gray-100 rounded-md">
                <input
                  type={showPassword ? "text" : "password"}
                  placeholder="Password"
                  className="w-full px-4 py-3 bg-transparent outline-none"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 text-gray-400"
                >
                  {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                </button>
              </div>
            </div>

            {error && <div className="text-red-600 text-sm text-center">{error}</div>}

            <button type="submit" className="w-full bg-[#8B2615] text-white py-3 rounded-md font-medium">
              Sign In
            </button>

            <div className="text-right">
              <Link href="/forgot-password" className="text-[#8B2615] text-sm">
                Forgot Password?
              </Link>
            </div>

            <div className="flex items-center">
              <div className="flex-1 border-t border-gray-300"></div>
              <span className="px-4 text-gray-500">or</span>
              <div className="flex-1 border-t border-gray-300"></div>
            </div>

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
      {/* ... same Google logo paths ... */}
    </svg>
  )
}
