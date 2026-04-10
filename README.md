# 🌱 Automated Greenhouse Management System (AGMS)

A cloud-native, microservice-based platform designed to automate greenhouse operations using real-time environmental data and rule-based decision-making.

---

# 📌 Overview

The **Automated Greenhouse Management System (AGMS)** enables farmers to monitor and control greenhouse conditions efficiently. The system integrates with an external IoT data provider to fetch live temperature and humidity data, processes it through a rule engine, and triggers automated actions to maintain optimal growing conditions.

---

# 🎯 Key Features

* 🌿 Zone-based greenhouse management
* 🌡️ Real-time sensor data integration
* 🤖 Automated decision-making (Fan/Heater control)
* 🌱 Crop lifecycle tracking
* 🔐 Centralized JWT-based security
* ☁️ Cloud-native microservices architecture

---

# 🏗️ System Architecture

The system follows a **microservices architecture** with centralized configuration and service discovery.

### Infrastructure Services

* **Config Server** – Centralized configuration management
* **Eureka Server** – Service discovery
* **API Gateway** – Routing and security (JWT)

### Domain Microservices

* **Zone Service** – Manages greenhouse zones and thresholds
* **Sensor Service** – Fetches external IoT data
* **Automation Service** – Rule engine for automated actions
* **Crop Service** – Manages crop inventory and lifecycle

---

# ⚙️ Tech Stack

| Layer             | Technology                     |
| ----------------- | ------------------------------ |
| Backend           | Spring Boot / Node.js / Python |
| Service Discovery | Eureka                         |
| API Gateway       | Spring Cloud Gateway           |
| Config Management | Spring Cloud Config            |
| Communication     | OpenFeign / REST               |
| Security          | JWT                            |
| Database          | MySQL / MongoDB / PostgreSQL   |
| Testing           | Postman                        |

---

# 🚀 Getting Started

## 🔧 Prerequisites

* Java 17+
* Maven / Gradle
* Git
* Postman
* Docker (optional)

---

## ▶️ Startup Order (IMPORTANT)

Start services in the following order:

### 1. Config Server

```bash
cd config-server-location
mvn spring-boot:run
```

---

### 2. Eureka Server

```bash
cd eureka-server-location
mvn spring-boot:run
```

Access dashboard:

```
http://localhost:8761
```

---

### 3. API Gateway

```bash
cd api-gateway-location
mvn spring-boot:run
```

---

### 4. Domain Microservices

Start each service:

```bash
cd service-location
mvn spring-boot:run


# 🔄 System Workflow

1. Sensor Service fetches telemetry data every 10 seconds
2. Data is sent to Automation Service
3. Automation Service:

   * Fetches zone thresholds
   * Applies rules
4. Action is triggered:

   * TURN_FAN_ON
   * TURN_HEATER_ON
5. Logs are stored and available via API

---

# 🌍 External IoT API Integration

### Base URL

```
http://104.211.95.241:8080/api
```

### Authentication Flow

1. Register user
2. Login → receive JWT token
3. Use token for all API requests

---

# 🔐 Security

* JWT-based authentication handled at **API Gateway**
* All incoming requests must include:

```
Authorization: Bearer <token>
```

* Unauthorized requests return:

```
401 Unauthorized
```

---

# 🔗 API Endpoints (Summary)

## Zone Service

* `POST /api/zones`
* `GET /api/zones/{id}`
* `PUT /api/zones/{id}`
* `DELETE /api/zones/{id}`

## Sensor Service

* `GET /api/sensors/latest`

## Automation Service

* `POST /api/automation/process`
* `GET /api/automation/logs`

## Crop Service

* `POST /api/crops`
* `PUT /api/crops/{id}/status`
* `GET /api/crops`

---

# 🧪 Testing

* Use **Postman Collection** (included in repo)
* Validate:

  * Zone creation
  * Sensor data ingestion
  * Automation triggers
  * Crop lifecycle updates

---

# 📸 Documentation

* Include **Eureka Dashboard screenshot** in `/docs`
* Export and include **Postman collection (.json)**

---

# 🧠 Business Rules

* `minTemp < maxTemp` (mandatory validation)
* If `temp > maxTemp` → TURN_FAN_ON
* If `temp < minTemp` → TURN_HEATER_ON

---

# ✅ Final Checklist

✔ Config Server running
✔ Eureka services registered
✔ Gateway routing working
✔ JWT security implemented
✔ Scheduler fetching data
✔ Automation rules triggered
✔ Postman collection included
✔ Documentation complete

---

🚀 *AGMS brings smart agriculture to life through automation and scalable cloud architecture.*
