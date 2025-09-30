# 🚀 ISS Real-Time Tracker

![Java](https://img.shields.io/badge/Java-17-blue?logo=java) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green?logo=spring) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql) ![Redis](https://img.shields.io/badge/Redis-7-orange?logo=redis) ![Docker](https://img.shields.io/badge/Docker-24-blue?logo=docker)

🌌 **Track the International Space Station in real-time!**  
A production-ready backend system built with **Java 17 + Spring Boot 3**, exposing **REST APIs**, a **dual-alert system**, and **historical data tracking**.  
<img width="1280" height="964" alt="image" src="https://github.com/user-attachments/assets/0ab698a7-57de-466c-9f56-ccc8578ddc7a" />

---

## ✨ Features

- **🌐 Real-Time Data Pipeline**  
  - Fetches ISS location **every 5 seconds**, caches in Redis, and persists to PostgreSQL.

- **⚠️ Dual-Alert System**
  - **⏰ 30-Minute Advance Alert**: Runs every 15 mins to warn if the ISS will pass a user location within 30 minutes.  
  - **🚨 Real-Time Flyover Alert**: Checks every 5 seconds if ISS is within 100km of any alert location and triggers instant notifications.

- **📝 Comprehensive REST API**  
  - Access current ISS position, historical data (paginated), upcoming passes, and manage alerts.

- **🔒 Admin Endpoints**  
  - Monitor **application health** and control **data pipeline** (pause/resume).

- **🐳 Fully Containerized**  
  - Runs Spring Boot + PostgreSQL + Redis via Docker Compose for easy deployment.

- **📖 Interactive API Docs**  
  - Swagger UI integrated for quick API exploration.

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Spring Boot 3 |
| Database | PostgreSQL |
| Cache | Redis |
| Containerization | Docker, Docker Compose |
| API Documentation | Swagger (springdoc-openapi) |
| Testing | JUnit 5, Testcontainers |

---

## 📋 Prerequisites

- Java 17+  
- Maven 3.8+  
- Docker & Docker Compose  

---

## 🚀 How to Run

### 1️⃣ Docker Compose (Recommended)

```bash
docker-compose up --build
````

* App URL: [http://localhost:8081](http://localhost:8081)

### 2️⃣ Local Maven Run

```bash
docker-compose up -d postgres redis
./mvnw spring-boot:run
```

---

## 📡 API Endpoints

* **Swagger UI**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

### 🔑 Key Endpoints

| Endpoint                          | Description                        |
| --------------------------------- | ---------------------------------- |
| `GET /api/iss/current`            | Live ISS position                  |
| `GET /api/iss/history`            | Paginated history of positions     |
| `GET /api/iss/passes/{lat}/{lon}` | Upcoming pass predictions          |
| `POST /api/alerts`                | Create a location alert            |
| `GET /api/analytics/orbit-stats`  | Orbital statistics                 |
| `POST /admin/pipeline/pause`      | Pause data collection (Admin only) |

---

## ⚙️ Configuration & Credentials

Config file: `src/main/resources/application.properties`

**Users:**

| Role     | Username | Password  |
| -------- | -------- | --------- |
| Standard | user     | password  |
| Admin    | admin    | adminpass |

---

💡 **Tip:** The ISS travels at ~28,000 km/h 🌍 — our tracker updates every 5 seconds, so you can see near-real-time flyovers!
<img width="319" height="180" alt="image" src="https://github.com/user-attachments/assets/bce534e0-f20d-4f5e-a2f9-b9b49d605607" />

---


