"use client"

import { useState } from "react"
import Link from "next/link"
import { Eye, EyeOff, Mail, Phone } from "lucide-react"
import { useRouter } from "next/navigation"
import { useAuth } from "@/context/AuthContext";

export default function Login() {
  const [showPassword, setShowPassword] = useState(false);
  const [contact, setContact] = useState("");
  const [password, setPassword] = useState("");
  const [contactMethod, setContactMethod] = useState("email");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const success = await login(contact, password);
      
      if (!success) {
        setError("Invalid email or password");
        return;
      }

      // Get the user data from localStorage
      const userData = JSON.parse(localStorage.getItem("user"));
      
      // Redirect based on role
      if (userData.role === "ADMIN") {
        router.push("/admin");
      } else if (userData.role === "MANAGER") {
        router.push("/manager/dashboard");
      } else {
        router.push("/home");
      }
    } catch (e) {
      console.error("Login error:", e);
      setError("An error occurred during login. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

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
                  required
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
                  required
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

            <button 
              type="submit" 
              disabled={isLoading}
              className={`w-full bg-[#8B2615] text-white py-3 rounded-md font-medium hover:bg-[#7a1f12] transition-colors ${isLoading ? 'opacity-50 cursor-not-allowed' : ''}`}
            >
              {isLoading ? 'Signing in...' : 'Sign In'}
            </button>

            <div className="text-right">
              <Link href="/forgot-password" className="text-[#8B2615] text-sm hover:text-[#7a1f12]">
                Forgot Password?
              </Link>
            </div>
          </form>
        </div>
      </div>

      <footer className="text-center py-4 text-sm text-gray-600 border-t">
        <div>(C) 2025 Maverick, Inc</div>
      </footer>
    </div>
  )
}
