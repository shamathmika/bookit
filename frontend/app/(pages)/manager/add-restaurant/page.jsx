"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import { addRestaurant } from "@/constants/apis";

const AddRestaurantPage = () => {
  const { user } = useAuth();
  const router = useRouter();

  // Function to convert 24-hour time to 12-hour format with AM/PM
  const convertTo12HourFormat = (time24) => {
    const [hours, minutes] = time24.split(':');
    const hour = parseInt(hours, 10);
    const period = hour >= 12 ? 'PM' : 'AM';
    const hour12 = hour % 12 || 12; // Convert 0 to 12 for 12 AM
    return `${hour12.toString().padStart(2, '0')}:${minutes} ${period}`;
  };

  const [form, setForm] = useState({
    name: "",
    description: "",
    photos: [],
    address: {
      street: "",
      city: "",
      state: "",
      zipCode: ""
    },
    phoneNumber: "",
    cuisine: "",
    costRating: 1,
    openingTime: "09:00",
    closingTime: "22:00"
  });

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    
    if (name === "photos" && files) {
      const newPhotos = Array.from(files).map(file => {
        return new Promise((resolve) => {
          const reader = new FileReader();
          reader.onload = (ev) => resolve(ev.target.result);
          reader.readAsDataURL(file);
        });
      });
      
      Promise.all(newPhotos).then(photoData => {
        setForm(f => ({ ...f, photos: [...f.photos, ...photoData] }));
      });
    } else if (name.startsWith("address.")) {
      const addressField = name.split(".")[1];
      setForm(f => ({
        ...f,
        address: {
          ...f.address,
          [addressField]: value
        }
      }));
    } else {
      setForm(f => ({ ...f, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      // Get the photo files from the input
      const photoInput = document.querySelector('input[name="photos"]');
      const photoFiles = photoInput?.files;

      // Create restaurant request object with converted times
      const restaurantRequest = {
        managerId: user.id,
        name: form.name,
        description: form.description,
        address: form.address,
        phoneNumber: form.phoneNumber,
        cuisine: form.cuisine,
        costRating: form.costRating,
        openingTime: convertTo12HourFormat(form.openingTime),
        closingTime: convertTo12HourFormat(form.closingTime)
      };

      // Create FormData for multipart/form-data
      const formData = new FormData();
      Object.entries(restaurantRequest).forEach(([key, value]) => {
        if (typeof value === 'object') {
          formData.append(key, JSON.stringify(value));
        } else {
          formData.append(key, value);
        }
      });

      // Add photos to FormData
      if (photoFiles) {
        Array.from(photoFiles).forEach(file => {
          formData.append('photos', file);
        });
      }

      await addRestaurant(formData);
      alert('Restaurant request sent successfully!');
      router.push("/manager/dashboard");
    } catch (error) {
      console.error('Error submitting restaurant:', error);
      alert('Error submitting restaurant request. Please try again.');
    }
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
            <label className="block text-sm font-medium mb-1">Cuisine</label>
            <input name="cuisine" value={form.cuisine} onChange={handleChange} required className="w-full border rounded px-3 py-2" />
          </div>
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Address</label>
          <div className="grid grid-cols-2 gap-4">
            <input name="address.street" value={form.address.street} onChange={handleChange} placeholder="Street" required className="w-full border rounded px-3 py-2" />
            <input name="address.city" value={form.address.city} onChange={handleChange} placeholder="City" required className="w-full border rounded px-3 py-2" />
            <input name="address.state" value={form.address.state} onChange={handleChange} placeholder="State" required className="w-full border rounded px-3 py-2" />
            <input name="address.zipCode" value={form.address.zipCode} onChange={handleChange} placeholder="ZIP Code" required className="w-full border rounded px-3 py-2" />
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4 mb-4">
          <div>
            <label className="block text-sm font-medium mb-1">Phone Number</label>
            <input name="phoneNumber" value={form.phoneNumber} onChange={handleChange} required className="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Cost Rating (1-5)</label>
            <input 
              type="number" 
              name="costRating" 
              value={form.costRating} 
              onChange={handleChange} 
              min="1" 
              max="5" 
              required 
              className="w-full border rounded px-3 py-2" 
            />
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
              <input type="time" name="openingTime" value={form.openingTime} onChange={handleChange} className="border rounded px-2 py-1" />
              <span>-</span>
              <input type="time" name="closingTime" value={form.closingTime} onChange={handleChange} className="border rounded px-2 py-1" />
            </div>
          </div>
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium mb-1">Restaurant Photos</label>
          <input 
            type="file" 
            name="photos" 
            accept="image/*" 
            multiple 
            onChange={handleChange} 
            className="block" 
          />
          <div className="text-xs text-slate-500 mt-1">Upload multiple high-quality images of your restaurant.</div>
          {form.photos.length > 0 && (
            <div className="mt-2 grid grid-cols-3 gap-2">
              {form.photos.map((photo, index) => (
                <img key={index} src={photo} alt={`Restaurant photo ${index + 1}`} className="w-full h-24 object-cover rounded" />
              ))}
            </div>
          )}
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