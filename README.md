# 🛒 ShopSphere API (THIS PROJECT IS IN PROGRESS)

A production-ready E-commerce Backend built using **Spring Boot**.
This project demonstrates clean architecture, secure authentication, and scalable backend design for modern web applications.

---

## 🚀 Features

* 🔐 JWT-based Authentication & Authorization
* 👤 Role-Based Access Control (USER / ADMIN)
* 📦 Product & Category Management
* 🛒 Cart & Checkout System
* 📑 Order Management & History
* 🔍 Search, Filtering & Pagination
* ✅ Input Validation & Global Exception Handling
* 📊 Optimized SQL Queries for Analytics

---

## 🧱 Tech Stack

* **Backend:** Spring Boot, Spring Security
* **Database:** PostgreSQL
* **ORM:** Hibernate / JPA
* **Authentication:** JWT (JSON Web Tokens)
* **Build Tool:** Maven
* **API Testing:** Postman / Swagger
* **Frontend:** Next.js

---

## 📂 Project Structure

```
src/main/java/com/shopsphere
│
├── controller      # REST Controllers
├── service         # Business Logic
├── repository      # Data Access Layer
├── dto             # Data Transfer Objects
├── entity          # Database Entities
├── security        # JWT & Authentication
├── exception       # Global Exception Handling
└── config          # Configuration Classes
```

---

## 🔑 API Highlights

### Auth

* `POST /api/auth/register`
* `POST /api/auth/login`

### Products

* `GET /api/products`
* `POST /api/products`
* `PUT /api/products/{id}`
* `DELETE /api/products/{id}`

### Cart

* `POST /api/cart/add`
* `GET /api/cart`
* `DELETE /api/cart/remove/{id}`

### Orders

* `POST /api/orders/checkout`
* `GET /api/orders`
* `GET /api/orders/{id}`

---

## 📊 Key Functionalities

* Efficient handling of relational data using **JPA & Hibernate**
* Secure endpoints using **Spring Security & JWT**
* Scalable design following **layered architecture**
* Optimized queries for:

  * User order history
  * Top-selling products
  * Sales insights

---

## 🌐 Deployment

> Coming soon...

---

## 🧪 Running Locally

```bash
# Clone the repository
git clone https://github.com/arpan-v/shopsphere.git

# Navigate to project folder
cd shopsphere/backend

# Run the application
mvn spring-boot:run
```

---


## 👨‍💻 Author

Developed by **Arpan-V**

---
