# Food Delivery System - Traditional Spring MVC

A full-stack web application for online food ordering and delivery management built using Spring Boot with Traditional MVC architecture and Thymeleaf template engine.

---

## 📋 Table of Contents

- [Project Description](#project-description)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [MVC Architecture](#mvc-architecture)
- [Database ERD](#database-erd)
- [How to Run](#how-to-run)
- [URL Mappings](#url-mappings)
- [Troubleshooting](#troubleshooting)

---

## 📝 Project Description

Food Delivery System is a comprehensive server-side rendered web application that connects customers with restaurants for seamless food ordering. Users can browse restaurants, manage their cart, place orders with multiple payment options, and track deliveries. The application follows Traditional Spring MVC pattern with server-side rendering using Thymeleaf templates.

### Key Objectives

- Provide server-side rendered pages for better SEO and performance
- Enable seamless food ordering experience with session-based cart management
- Implement secure authentication with Spring Security
- Automate delivery agent assignment
- Ensure secure payment processing
- Maintain strong data security with BCrypt password encryption

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.1.5
- **Language:** Java 17
- **Template Engine:** Thymeleaf
- **Database:** MySQL 8.0
- **ORM:** Hibernate/JPA
- **Security:** Spring Security with BCrypt
- **Build Tool:** Maven 3.x

### Frontend
- **Markup:** HTML5 with Thymeleaf
- **Styling:** CSS3
- **Scripting:** Vanilla JavaScript
- **HTTP Client:** Fetch API

---

## ✨ Features

### Customer Features
- User registration and secure login
- Browse restaurants by cuisine and ratings
- View detailed menu with prices
- Real-time cart management
- Multiple payment options (Card, UPI, COD)
- Order tracking with delivery time
- Complete order history
- Profile management

### Restaurant Features
- Restaurant profile with cuisine and ratings
- Menu management with items and pricing

### System Features
- Session-based authentication
- Server-side form validation
- Responsive design
- Cart persistence during session

---

## 📁 Project Structure
```
Food-delivery-MVC/
├── pom.xml
├── README.md
├── database/
│   ├── schema.sql
│   └── sample-data.sql
└── src/
    ├── main/
    │   ├── java/com/fooddelivery/
    │   │   ├── FoodDeliveryApplication.java
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java
    │   │   ├── controller/
    │   │   │   ├── HomeController.java
    │   │   │   ├── RestaurantController.java
    │   │   │   ├── OrderController.java
    │   │   │   ├── UserController.java
    │   │   │   └── CartController.java
    │   │   ├── service/
    │   │   │   ├── UserService.java
    │   │   │   ├── RestaurantService.java
    │   │   │   ├── OrderService.java
    │   │   │   └── PaymentService.java
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java
    │   │   │   ├── RestaurantRepository.java
    │   │   │   ├── MenuItemRepository.java
    │   │   │   ├── OrderRepository.java
    │   │   │   ├── OrderItemRepository.java
    │   │   │   ├── PaymentRepository.java
    │   │   │   └── DeliveryAgentRepository.java
    │   │   └── model/
    │   │       ├── User.java
    │   │       ├── Restaurant.java
    │   │       ├── MenuItem.java
    │   │       ├── Order.java
    │   │       ├── OrderItem.java
    │   │       ├── Payment.java
    │   │       └── DeliveryAgent.java
    │   └── resources/
    │       ├── application.properties
    │       ├── templates/
    │       │   ├── landing.html
    │       │   ├── login.html
    │       │   ├── signup.html
    │       │   ├── home.html
    │       │   ├── menu.html
    │       │   ├── checkout.html
    │       │   ├── orders.html
    │       │   └── profile.html
    │       └── static/
    │           ├── css/
    │           │   └── style.css
    │           └── js/
    │               ├── cart.js
    │               ├── menu.js
    │               └── checkout.js
    └── test/
        └── java/
```

---

## 🏗️ MVC Architecture
```
┌──────────────────────────────────────┐
│   View Layer (Thymeleaf Templates)  │
│   - Server-side HTML rendering      │
└─────────────┬────────────────────────┘
              │
              ▼
┌──────────────────────────────────────┐
│   Controller Layer                   │
│   - @Controller                      │
│   - Handle HTTP requests             │
└─────────────┬────────────────────────┘
              │
              ▼
┌──────────────────────────────────────┐
│   Service Layer                      │
│   - @Service                         │
│   - Business logic                   │
└─────────────┬────────────────────────┘
              │
              ▼
┌──────────────────────────────────────┐
│   Repository Layer                   │
│   - JpaRepository                    │
│   - Database operations              │
└─────────────┬────────────────────────┘
              │
              ▼
┌──────────────────────────────────────┐
│   MySQL Database                     │
└──────────────────────────────────────┘
```

---

## 🗄️ Database ERD

### Tables

**users**
- id (PK), name, email, password, phone, address, city, postal_code, created_at, updated_at

**restaurants**
- id (PK), name, cuisine, rating, image_url, created_at

**menu_items**
- id (PK), restaurant_id (FK), name, description, price, created_at

**orders**
- id (PK), user_id (FK), restaurant_id (FK), total_amount, tax, delivery_fee, status, delivery_address, payment_method, delivery_agent, estimated_delivery_time, created_at, updated_at

**order_items**
- id (PK), order_id (FK), menu_item_id (FK), name, quantity, price, created_at

**payments**
- id (PK), order_id (FK), amount, payment_method, payment_status, transaction_id, created_at

**delivery_agents**
- id (PK), name, phone, rating, created_at

### Relationships

| Relationship | Type |
|-------------|------|
| Users → Orders | 1:N |
| Restaurants → Orders | 1:N |
| Restaurants → Menu Items | 1:N |
| Orders → Order Items | 1:N |
| Menu Items → Order Items | 1:N |
| Orders → Payments | 1:1 |

---

## 🚀 How to Run

### Prerequisites

- Java JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Git (optional)

### Setup Steps

#### 1. Clone Repository
```bash
git clone https://github.com/yourusername/Food-delivery-MVC.git
cd Food-delivery-MVC
```

#### 2. Setup Database
```bash
mysql -u root -p
```
```sql
CREATE DATABASE food_delivery;
USE food_delivery;
```

#### 3. Load Schema
```bash
mysql -u root -p food_delivery < database/schema.sql
```

#### 4. Configure Database

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_delivery
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.thymeleaf.cache=false

server.port=8080
```

#### 5. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

#### 6. Access Application

Open browser: `http://localhost:8080`

---

## 🔗 URL Mappings

### Public Routes

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/` | Landing page |
| GET | `/login` | Login page |
| POST | `/perform_login` | Process login |
| GET | `/signup` | Signup page |
| POST | `/signup` | Register user |

### Protected Routes

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/home` | Browse restaurants |
| GET | `/restaurants/{id}/menu` | View menu |
| GET | `/orders` | Order history |
| GET | `/orders/checkout` | Checkout page |
| POST | `/orders/place` | Place order |
| GET | `/profile` | View profile |
| POST | `/profile/update` | Update profile |
| GET | `/logout` | Logout |

### AJAX Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/cart/add` | Add to cart |
| POST | `/cart/remove` | Remove from cart |
| GET | `/cart/get` | Get cart |
| POST | `/cart/clear` | Clear cart |

---

## 🐛 Troubleshooting

### Application won't start

**Check:**
- MySQL is running
- Database credentials correct
- Port 8080 available
- Run `mvn clean install`

### Templates not found

**Check:**
- Files in `src/main/resources/templates/`
- Thymeleaf configuration
- File names match controller returns

### Database connection error

**Check:**
- MySQL credentials
- Database `food_delivery` exists
- MySQL running on port 3306

### Login not working

**Check:**
- SecurityConfig configured
- User exists in database
- Password encrypted with BCrypt

Open an issue for questions or support.
