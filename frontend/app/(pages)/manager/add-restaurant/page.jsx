"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useRestaurantContext } from "../context/RestaurantContext";

const AddRestaurantPage = () => {
  const { addRestaurant } = useRestaurantContext();
  const router = useRouter();
  const [form, setForm] = useState({
    name: "",
    address: "",
    city: "",
    phone: "",
    email: "",
    description: "",
    openingStart: "09:00",
    openingEnd: "22:00",
    image: "",
  });

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === "image" && files[0]) {
      const reader = new FileReader();
      reader.onload = (ev) => setForm((f) => ({ ...f, image: ev.target.result }));
      reader.readAsDataURL(files[0]);
    } else {
      setForm((f) => ({ ...f, [name]: value }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    addRestaurant({
      name: form.name,
      address: form.address,
      city: form.city,
      phone: form.phone,
      email: form.email,
      description: form.description,
      openingStart: form.openingStart,
      openingEnd: form.openingEnd,
      image: form.image,
    });
    router.push("/manager/dashboard");
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Add New Restaurant</h1>
      <form className="bg-white rounded-xl p-8 max-w-2xl" onSubmit={handleSubmit}>
        <div className="grid grid-cols-2 gap-4 mb-4">
          <div>
            <label className="block text-sm font-medium mb-1">Restaurant Name</label>
            <input name="name" value={form.name} onChange={handleChange} required className="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">City</label>
            <input name="city" value={form.city} onChange={handleChange} required className="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Address</label>
            <input name="address" value={form.address} onChange={handleChange} required className="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Email</label>
            <input name="email" value={form.email} onChange={handleChange} type="email" required className="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Phone Number</label>
            <input name="phone" value={form.phone} onChange={handleChange} required className="w-full border rounded px-3 py-2" />
          </div>
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Restaurant Description</label>
          <textarea name="description" value={form.description} onChange={handleChange} rows={2} className="w-full border rounded px-3 py-2" />
        </div>
        <div className="flex gap-4 mb-4">
          <div>
            <label className="block text-sm font-medium mb-1">Opening Hours</label>
            <div className="flex gap-2 items-center">
              <input type="time" name="openingStart" value={form.openingStart} onChange={handleChange} className="border rounded px-2 py-1" />
              <span>-</span>
              <input type="time" name="openingEnd" value={form.openingEnd} onChange={handleChange} className="border rounded px-2 py-1" />
            </div>
          </div>
        </div>
        <div className="mb-6">
          <label className="block text-sm font-medium mb-1">Restaurant Image</label>
          <input type="file" name="image" accept="image/*" onChange={handleChange} className="block" />
          <div className="text-xs text-slate-500 mt-1">Upload a high-quality image of your restaurant.</div>
        </div>
        <div className="flex gap-4 mt-8">
          <button type="button" onClick={() => router.back()} className="rounded-md px-6 py-2 border border-slate-300">Cancel</button>
          <button type="submit" className="bg-rose-700 hover:bg-rose-800 rounded-md px-6 py-2 text-white">Add Restaurant</button>
        </div>
      </form>
    </div>
  );
};

export default AddRestaurantPage; 