# Salon

A microservices-based salon booking and management system built with Spring Boot and Spring Cloud. The platform handles user accounts, salon listings, service offerings, categories, bookings, and payments.

## Features

- **User management** — Registration and profile management
- **Salon listings** — Create and manage salon profiles
- **Service offerings** — Define services with pricing per salon
- **Categories** — Organize services into categories
- **Booking system** — Schedule and manage appointments
- **Payment integration** — Payment order and link generation
- **API Gateway** — Single entry point for all services
- **Service discovery** — Eureka server for microservice registration

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 4, Spring Cloud |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Database | MySQL (per service) |
| Build Tool | Maven |

## Microservices

| Service | Folder | Purpose |
|---------|--------|---------|
| Eureka Server | `eureka-server/` | Service registry and discovery |
| API Gateway | `gateway-server/` | Routes requests to microservices |
| User Service | `user-service/` | User accounts and authentication |
| Salon Service | `salon-service/` | Salon profile management |
| Category Service | `catagory-service/` | Service category management |
| Service Offering | `service-offering/` | Salon services and pricing |
| Booking Service | `Booking-Service/` | Appointment booking |
| Payment Service | `payment-service/` | Payment processing |

## Project Structure

```
Salon/
├── eureka-server/
├── gateway-server/
├── user-service/
├── salon-service/
├── catagory-service/
├── service-offering/
├── Booking-Service/
└── payment-service/
```

## Getting Started

Start services in this order:

### 1. Eureka Server
```bash
cd eureka-server
./mvnw spring-boot:run
```

### 2. Individual Microservices
Start each service in a separate terminal:
```bash
cd user-service && ./mvnw spring-boot:run
cd salon-service && ./mvnw spring-boot:run
cd catagory-service && ./mvnw spring-boot:run
cd service-offering && ./mvnw spring-boot:run
cd Booking-Service && ./mvnw spring-boot:run
cd payment-service && ./mvnw spring-boot:run
```

### 3. API Gateway (Last)
```bash
cd gateway-server
./mvnw spring-boot:run
```

> Configure database connection details in each service's `src/main/resources/application.yml` before running.

## API Overview

| Service | Key Endpoints |
|---------|---------------|
| User Service | User CRUD, registration |
| Salon Service | Salon CRUD operations |
| Category Service | Category management |
| Service Offering | Services per salon |
| Booking Service | Create and manage bookings |
| Payment Service | Payment link generation |
