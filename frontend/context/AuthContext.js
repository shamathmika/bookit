// src/context/AuthContext.js
"use client";

import React, { createContext, useContext, useState, useEffect } from "react";
import { login as apiLogin, signUp } from "../constants/apis";
import { useRouter } from "next/navigation";

const AuthContext = createContext();

// Provides: { user, isLoggedIn, login(), logout() }
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  // on mount, rehydrate from localStorage
  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    const storedToken = localStorage.getItem("token");
    
    if (storedUser && storedToken) {
      try {
        const userData = JSON.parse(storedUser);
        if (userData.token === storedToken) {
          setUser(userData);
        } else {
          localStorage.removeItem("user");
          localStorage.removeItem("token");
        }
      } catch (error) {
        console.error("Error parsing stored user data:", error);
        localStorage.removeItem("user");
        localStorage.removeItem("token");
      }
    }
    setIsLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      console.log("Attempting login with:", { email, password });
      const response = await apiLogin({ email, password });
      console.log("Login response:", response);

      if (!response || !response.token) {
        throw new Error("Invalid response from server");
      }

      // Extract role from token or response
      const role = response.role || 'CUSTOMER'; // Default to CUSTOMER if role not provided

      const userData = {
        id: response.id,
        email: response.email,
        name: response.name,
        role: role,
        token: response.token,
      };

      console.log("Setting user data:", userData);
      setUser(userData);
      localStorage.setItem("user", JSON.stringify(userData));
      localStorage.setItem("token", response.token);
      return true;
    } catch (error) {
      console.error("Login error:", error);
      return false;
    }
  };

  const register = async (name, email, password) => {
    try {
      const response = await signUp({ name, email, password });
      
      if (!response || !response.token) {
        throw new Error("Invalid response from server");
      }

      const userData = {
        id: response.id,
        email: response.email,
        name: response.name,
        role: response.role || 'CUSTOMER',
        token: response.token,
      };

      setUser(userData);
      localStorage.setItem("user", JSON.stringify(userData));
      localStorage.setItem("token", response.token);
      return true;
    } catch (error) {
      console.error("Registration error:", error);
      return false;
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    router.push('/login');
  };

  const isLoggedIn = !!user;
  const isManager = user?.role === 'MANAGER';
  const isAdmin = user?.role === 'ADMIN';
  const isCustomer = user?.role === 'CUSTOMER';

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoggedIn,
        isLoading,
        isManager,
        isAdmin,
        isCustomer,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook for consuming auth
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
