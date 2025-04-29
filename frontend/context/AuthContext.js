// src/context/AuthContext.js
"use client";

import React, { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext();

// Provides: { user, isLoggedIn, login(), logout() }
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  // on mount, rehydrate from localStorage
  useEffect(() => {
    try {
      const stored = localStorage.getItem("user");
      if (stored) setUser(JSON.parse(stored));
    } catch (e) {
      console.error("Failed to parse stored user:", e);
    }
  }, []);

  function login(userData) {
    // userData = { id, name, email, phoneNumber, role, token }
    localStorage.setItem("token", userData.token);
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
  }

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoggedIn: !!user,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

// Custom hook for consuming auth
export function useAuth() {
  const ctx = useContext(AuthContext);
  if (ctx === undefined) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return ctx;
}
