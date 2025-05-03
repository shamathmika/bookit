// Get base URL from environment variable, fallback to localhost for development
const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// Helper function to handle API responses
const handleResponse = async (response) => {
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || 'Something went wrong');
  }
  return response.json();
};

// Helper function to create fetch options
const createFetchOptions = (method = 'GET', body = null, isFormData = false, requireAuth = true) => {
  const options = {
    method,
    headers: {}
  };

  if (!isFormData) {
    options.headers['Content-Type'] = 'application/json';
  }

  if (body) {
    options.body = isFormData ? body : JSON.stringify(body);
  }

  // Add auth token if required and available
  if (requireAuth) {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Authentication required');
    }
    options.headers['Authorization'] = `Bearer ${token}`;
  }

  return options;
};

// ───────────── AUTH ─────────────
export const login = (body) =>
  fetch(`${BASE_URL}/auth/signin`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
    .then(handleResponse);

export const signUp = (body) =>
  fetch(`${BASE_URL}/auth/signup`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
    .then(handleResponse);

// ───────────── USERS ─────────────
export const getUser = (userId) =>
  fetch(`${BASE_URL}/users/${userId}`)
    .then(handleResponse);

export const getUserReviews = (userId) =>
  fetch(`${BASE_URL}/users/${userId}/reviews`)
    .then(handleResponse);

export const getUserReservations = (userId) =>
  fetch(`${BASE_URL}/users/${userId}/reservations`)
    .then(handleResponse);

// ───────────── RESTAURANTS ─────────────
export const getAvailableTables = () =>
  fetch(`${BASE_URL}/restaurants/available-tables`)
    .then(handleResponse);

export const getRestaurantCategories = (location) =>
  fetch(`${BASE_URL}/restaurants/categories?location=${encodeURIComponent(location)}`)
    .then(handleResponse);

export const getRestaurantById = (id) =>
  fetch(`${BASE_URL}/restaurants/${id}`)
    .then(handleResponse);

export const searchRestaurants = (queryParams) =>
  fetch(`${BASE_URL}/restaurants/search?${queryParams}`)
    .then(handleResponse);

export const getRestaurantDetails = (id) =>
  fetch(`${BASE_URL}/restaurants/${id}`)
    .then(handleResponse);

export const getAvailableTimes = (id, formattedDate, people = 1) =>
  fetch(`${BASE_URL}/restaurants/${id}/available-times?date=${formattedDate}&people=${people}`)
    .then(handleResponse);

// ───────────── REVIEWS ─────────────
export const getReviewById = (reviewId) =>
  fetch(`${BASE_URL}/reviews/${reviewId}`)
    .then(handleResponse);

export const getStandaloneReviews = () =>
  fetch(`${BASE_URL}/reviews/reviews/standalone`)
    .then(handleResponse);

// ───────────── BOOKINGS ─────────────
export const createBooking = ({ restaurantId, userId, dateTime, people }) =>
  fetch(`${BASE_URL}/bookings/create?restaurantId=${restaurantId}&userId=${userId}&dateTime=${dateTime}&people=${people}`, 
    createFetchOptions('POST'))
    .then(handleResponse);

export const confirmBooking = (bookingId, type = 'EMAIL') =>
  fetch(`${BASE_URL}/bookings/${bookingId}/confirm?type=${type}`, 
    createFetchOptions('POST'))
    .then(handleResponse);

export const getBookingById = (bookingId) =>
  fetch(`${BASE_URL}/bookings/${bookingId}`)
    .then(handleResponse);

// ───────────── MANAGER ─────────────
export const addRestaurant = (formData) =>
  fetch(`${BASE_URL}/manager/restaurants/add-restaurant`, 
    createFetchOptions('POST', formData, true))
    .then(handleResponse);

export const getRestaurantsByManager = (managerId) =>
  fetch(`${BASE_URL}/manager/restaurants/restaurants-by-manager/${managerId}`)
    .then(handleResponse);

export const addTablesToRestaurant = (restaurantId, body) =>
  fetch(`${BASE_URL}/manager/tables/${restaurantId}/add-tables`, 
    createFetchOptions('POST', body))
    .then(handleResponse);

export const getTablesByRestaurant = (restaurantId) =>
  fetch(`${BASE_URL}/manager/tables/${restaurantId}`)
    .then(handleResponse);

// ───────────── ADMIN ─────────────
export const getAdminDashboardData = () =>
  fetch(`${BASE_URL}/admin/restaurants/dashboard`)
    .then(handleResponse);

export const getPendingRestaurants = () =>
  fetch(`${BASE_URL}/admin/restaurants/pending`)
    .then(handleResponse);

export const approveRestaurant = (restaurantId) =>
  fetch(`${BASE_URL}/admin/restaurants/${restaurantId}/approve`, 
    createFetchOptions('POST'))
    .then(handleResponse);

export const rejectRestaurant = (restaurantId) =>
  fetch(`${BASE_URL}/admin/restaurants/${restaurantId}/reject`, 
    createFetchOptions('POST'))
    .then(handleResponse);

// ───────────── ANALYTICS ─────────────
export const getPopularBookingSlots = () =>
  fetch(`${BASE_URL}/booking-stats/analytics/popular-slots`)
    .then(handleResponse);

export const getMonthlyBookingStats = () =>
  fetch(`${BASE_URL}/booking-stats/analytics/monthly`)
    .then(handleResponse);
