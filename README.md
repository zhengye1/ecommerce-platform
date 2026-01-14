# 🛒 E-Commerce Platform

A **FAANG-grade** event-driven e-commerce platform built with **Spring Boot 3** and **React 18**.

## 🏗️ Architecture

- **Backend**: Spring Boot 3.5.9 + Java 21 (Clean Architecture)
- **Frontend**: React 18 + TypeScript + Vite + Zustand
- **Database**: MySQL 8.0 (primary) + DynamoDB (events)
- **Cache**: Redis
- **Message Queue**: AWS SQS/SNS
- **CI/CD**: GitHub Actions + SonarQube

## 📁 Project Structure

```
ecommerce-platform/
├── backend/                 # Spring Boot microservices
│   ├── common/              # Shared modules
│   ├── user-service/        # User & authentication
│   ├── product-service/     # Product catalog
│   ├── order-service/       # Order management
│   ├── payment-service/     # Payment processing
│   ├── inventory-service/   # Stock management
│   └── notification-service/# Email/SMS notifications
├── frontend/                # React application
├── docs/                    # Documentation
└── .github/                 # CI/CD workflows
```

## 🚀 Quick Start

### Prerequisites

- Java 21+
- Node.js 20+
- MySQL 8.0
- Maven 3.9+

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run -pl user-service
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## 📖 Documentation

See [docs/](./docs/) for detailed documentation:

- [Architecture Deep Dive](./docs/architecture-deep-dive.md)
- [Project Structure](./docs/project-structure.md)
- [Database Schema](./docs/database-schema.md)
- [CI/CD Pipeline](./docs/cicd-pipeline.md)

## 📄 License

MIT License - see [LICENSE](./LICENSE) for details.