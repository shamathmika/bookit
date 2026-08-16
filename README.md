
# BOOKIT – Reserve Tables at Restaurants Near You

**BOOKIT** is a full-stack web application that enables customers to search for, book, and manage reservations at local restaurants. It also provides role-based dashboards for restaurant managers and administrators.

---

## 🧠 Design and Documentation

- [Architecture Diagram](https://drive.google.com/file/d/1Q6Y_G6UqJ4IjdxALsZNTKnXkTw5BYmGf/view?usp=sharing)  
- [Class Diagram](https://drive.google.com/file/d/1HSTc2PXmXjUZQByO7DM0Dz6unUvjyvv5/view?usp=sharing)  
- [Activity Diagram](https://drive.google.com/file/d/1DZKmHHuBFLkwHV8Z79T8U1ZggTlJWXuJ/view?usp=sharing)  
- [DB Schema (Google Doc)](https://docs.google.com/document/d/1VPLPccsg1l33Uzcl9F5VwUg11EFMA4N3II2IwibCpAQ/edit?tab=t.0)  
- [MongoDB Sample Data](https://drive.google.com/file/d/1H5ZKha7Na9XGZsErLApgVOEVuUxYx4w2/view?usp=sharing)
- [Component Diagram](https://docs.google.com/document/d/1bsJhaqyonQEhmoKLUy4EoYucErJCujauD5KMgFh-EFQ/edit?tab=t.0)
- [Deployment Diagram](https://docs.google.com/document/d/1bsJhaqyonQEhmoKLUy4EoYucErJCujauD5KMgFh-EFQ/edit?tab=t.0)
  

---

## 🎨 UI Wireframes

- [Customer UI](https://www.figma.com/design/oD7iFl2Vo2mKEJdKrAMBOX/CMPE-202-Project---Mavericks---UI-Wireframes---Customer?node-id=0-1)  
- [Manager UI](https://www.figma.com/design/iVyQgk1tTMJgU2CzBtyX1e/demo-ui-wireframe-restaurant-manager?node-id=1-2)  
- [Admin UI](https://www.figma.com/design/IfnvPNELrO9PSmxRpXhIJN/admin-wireframe?node-id=0-1)  

---

## 🚀 Deployment

- **Frontend** deployed on Vercel
- **Backend APIs** deployed on AWS EC2   
- **Database:** MongoDB
- **Dockerized** services for ease of deployment  

---

## 🧩 Features by Role

### Customer
- Sign up, log in, log out
- Search restaurants with filters (location, Zip, Name, Time, #People)
- Book and cancel reservations
- View profile and edit
- View Reservations
- View Review
- Leave reviews and ratings
- View restaurants on Google Maps
- Receive email confirmations

### Restaurant Manager
- Register and manage restaurant listings
- Add/update restaurant details, tables, time slots, images
- View and manage own listings

### Admin
- Approve or reject new restaurant submissions
- Remove listings
- View analytics dashboard

  
---

## 🧪 Testing

Features were verified against the user stories, and integration points between the frontend and backend were validated through test bookings and admin flows, backed by internal testing and code reviews.
