# My Projects Portfolio

A collection of full-stack applications and Java projects built during my learning journey. Each project lives in its own folder with source code and a dedicated README.

## Projects Overview

| Project | Type | Tech Stack | Description |
|---------|------|------------|-------------|
| [Blood-Donor](./Blood-Donor/) | Full-Stack Web App | React, Spring Boot, MySQL, Docker | Blood donation platform connecting donors, hospitals, and blood banks |
| [Salon](./Salon/) | Microservices | Spring Boot, Eureka, API Gateway | Salon booking and management system with multiple services |
| [TaskManager](./TaskManager/) | Console App | Java | Command-line task manager with user login and CRUD operations |
| [TransitOps](./TransitOps-Smart-Transport-Operations-Platform/) | Full-Stack Web App | React, TypeScript, Spring Boot, MySQL, Docker | Smart transport operations platform for fleet management |
| [Mini-ERP](./Mini-ERP/) | Full-Stack Web App | React, Spring Boot, MySQL, Docker | Shiv Furniture Works demand-to-delivery ERP portal |

## Repository Structure

```
All-Projects/
├── README.md                          # This file
├── Blood-Donor/
│   ├── Blood-Donor-Backend/           # Spring Boot REST API
│   ├── Blood-Donor-FrontEnd/          # React + Vite frontend
│   └── docker-compose.yml
├── Salon/
│   ├── eureka-server/                 # Service discovery
│   ├── gateway-server/                # API Gateway
│   ├── user-service/
│   ├── salon-service/
│   ├── Booking-Service/
│   ├── service-offering/
│   ├── catagory-service/
│   └── payment-service/
├── TaskManager/                       # Java console application
├── TransitOps-Smart-Transport-Operations-Platform/
│   ├── TransitOps_backend/            # Spring Boot REST API
│   ├── TransitOps_frontend/           # React + TypeScript frontend
│   └── docker-compose.yml
└── Mini-ERP/
    ├── Frontend/                      # React + Vite frontend
    ├── Backend/Mini-ERP/              # Spring Boot REST API
    └── docker-compose.yml
```

## Quick Start

Each project has its own README with detailed setup instructions. Here is a quick reference:

### Blood-Donor
```bash
cd Blood-Donor
docker-compose up --build
# Frontend: http://localhost:5174 | Backend: http://localhost:8081
```

### TransitOps
```bash
cd TransitOps-Smart-Transport-Operations-Platform
# Create .env file with MySQL and JWT settings
docker-compose up --build
# Frontend: http://localhost:5173 | Backend: http://localhost:8081
```

### Salon (Microservices)
Start each service individually via Maven. See [Salon README](./Salon/README.md) for service order and ports.

### TaskManager
```bash
cd TaskManager
javac *.java
java Main
```

### Mini-ERP
```bash
cd Mini-ERP
docker-compose up --build
# Frontend: http://localhost:3000 | Backend: http://localhost:8080
```

## Prerequisites

- **Java 17+** — for Spring Boot backends and TaskManager
- **Node.js 18+** — for React frontends
- **Docker & Docker Compose** — for Blood-Donor and TransitOps
- **Maven** — for Spring Boot projects

## Author

Rithik
