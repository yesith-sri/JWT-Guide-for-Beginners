# 🔐 JWT Authentication System with Spring Boot

A complete JWT authentication implementation for REST APIs using Spring Boot.

---

## 📋 Features

- ✅ User registration and login  
- ✅ JWT token generation and validation  
- ✅ BCrypt password hashing  
- ✅ Role-based access control  
- ✅ Protected endpoints  
- ✅ Stateless authentication  

---

## 🛠️ Tech Stack

- Java 17+  
- Spring Boot 3.2.0  
- Spring Security 6.x  
- MySQL 8.0+  
- JWT (JJWT) 0.12.3  
- Maven  

---

## 🚀 Quick Start

### 📌 Prerequisites

- Java 17+  
- MySQL 8.0+  
- Maven 3.8+  

---

## ⚙️ Setup

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/jwt-authentication-system.git
cd jwt-authentication-system

```

### 2. Configure Application (application.yml)
```bash
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/voting_db
    username: root
    password: your_password
```

### 3. Build and Run
```bash
mvn clean install
mvn spring-boot:run

```

## 📡 API Endpoints
###  🔓 Authentication (Public)
➤ Register User
```bash
POST /api/auth/register
```

```bash
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "Password@123"
}

```
➤ Login

```bash
POST /api/auth/login
```

```bash
{
  "username": "john_doe",
  "password": "Password@123"
}

```
##  🔐 Protected Endpoints (Require Token)
###   ➤ Get Profile

```bash
GET /api/users/profile
Authorization: Bearer <TOKEN>
```

###   ➤ Admin Dashboard (ROLE_ADMIN only)

```bash
GET /api/users/admin/dashboard
Authorization: Bearer <TOKEN>
```

###   ➤ Moderator Panel (ROLE_ADMIN or ROLE_MODERATOR)

```bash
GET /api/users/moderator/panel
Authorization: Bearer <TOKEN>
```

## 📝 Sample Users

Default password for all users: `pass123`

| Username | Email | Role |
|----------|-------|------|
| admin | admin@example.com | ROLE_ADMIN |
| john | john@example.com | ROLE_USER |
| moderator | moderator@example.com | ROLE_MODERATOR |

## 📁 Project Structure

```jwt-authentication-system/
├── src/main/java/com/edu/yesh/
│   ├── config/
│   ├── controller/
│   ├── service/
│   ├── security/
│   ├── filter/
│   ├── entity/
│   ├── repository/
│   └── dto/
├── src/main/resources/
│   └── application.yml
└── pom.xml
```


## 🔐 Security Features

- 🔒 BCrypt password hashing
- 🔑 HMAC-SHA512 JWT signing
- ⏳ 24-hour token expiration
- 👥 Role-based access control
- ✔️ Input validation
- ⚠️ Error handling
