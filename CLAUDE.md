# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Event-Driven E-Commerce Platform - a FAANG-grade microservices architecture using Spring Boot (backend) and React (frontend) in a monorepo structure.

## Technology Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 3.5.9 + Java 21 |
| Frontend | React 18 + TypeScript 5.x |
| Build (Backend) | Maven 3.9.x (multi-module) |
| Build (Frontend) | Vite 5.x |
| State Management | Zustand + React Query |
| UI Components | shadcn/ui + Tailwind CSS |
| Primary Database | MySQL 8.0 (Amazon RDS) |
| NoSQL | Amazon DynamoDB |
| Cache | Redis 7 (ElastiCache) |
| Messaging | Amazon SQS + SNS |
| Container Platform | Amazon ECS Fargate |

## Common Commands

### Backend (from `backend/` directory)

```bash
# Build all services
mvn clean compile

# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest='**/unit/**'

# Run only integration tests
mvn test -Dtest='**/integration/**'

# Run a specific service test
mvn test -pl user-service -Dtest=UserServiceTest

# Generate coverage report
mvn jacoco:report

# Check coverage threshold (80%)
mvn jacoco:check -Djacoco.check.lineRatio=0.80

# Run SonarQube analysis
mvn verify sonar:sonar -Dsonar.projectKey=ecommerce-backend

# Run OWASP dependency check
mvn org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=7

# Start a single service
mvn spring-boot:run -pl user-service
```

### Frontend (from `frontend/` directory)

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Run tests
npm test

# Run tests with coverage
npm run test:coverage

# Lint code
npm run lint

# Type check
npm run typecheck

# Build for production
npm run build

# Run E2E tests (Cypress)
npm run cypress:run
```

### Local Development

```bash
# Start all infrastructure (MySQL, Redis, LocalStack)
docker-compose up -d

# Setup local environment
./scripts/setup-local.sh

# Run all tests across services
./scripts/run-all-tests.sh

# Generate API client from OpenAPI
./scripts/generate-api-client.sh
```

## Architecture

### Monorepo Structure

```
ecommerce-platform/
├── backend/                    # All Spring Boot services
│   ├── pom.xml                # Parent POM (multi-module)
│   ├── common/                # Shared libraries
│   │   ├── common-core/       # Core utilities, exceptions
│   │   ├── common-security/   # JWT, security filters
│   │   ├── common-messaging/  # Event publishers/consumers
│   │   └── common-test/       # Test utilities, fixtures
│   ├── user-service/
│   ├── product-service/
│   ├── order-service/
│   ├── payment-service/
│   ├── inventory-service/
│   ├── notification-service/
│   └── api-gateway/
├── frontend/                   # React application
├── infrastructure/
│   ├── terraform/             # AWS infrastructure
│   ├── docker/                # Docker configurations
│   └── k8s/                   # Kubernetes manifests
├── docs/                       # Documentation
└── scripts/                    # Utility scripts
```

### Microservice Internal Structure (Clean Architecture)

Each microservice follows Hexagonal/Clean Architecture:

```
service/
├── domain/                     # Core business logic (no dependencies)
│   ├── model/                 # Domain entities
│   ├── repository/            # Port interfaces
│   ├── service/               # Domain services
│   └── event/                 # Domain events
├── application/                # Use cases, DTOs, mappers
│   ├── dto/request/
│   ├── dto/response/
│   ├── mapper/
│   └── usecase/
├── infrastructure/             # External adapters
│   ├── persistence/           # JPA repositories, entities
│   ├── messaging/             # SQS/SNS publishers/consumers
│   └── cache/                 # Redis adapters
├── api/                        # REST controllers
│   └── controller/
└── config/                     # Spring configuration
```

### Event-Driven Flow

Services communicate asynchronously via SNS/SQS:
- **SNS Topics**: `order-events`, `payment-events`, `inventory-events`
- **SQS Queues**: Per-service consumers for each topic
- **Saga Pattern**: Order placement uses choreography-based sagas with compensating transactions

### Database Strategy (Polyglot Persistence)

| Database | Use Case | Data |
|----------|----------|------|
| MySQL (RDS) | ACID transactions | Users, Products, Orders, Payments |
| DynamoDB | Event store, sessions | Events, Audit logs, Sessions |
| Redis | Cache, locking | Product cache, Cart, Distributed locks |

## Quality Gates

All blocking thresholds for CI/CD:

| Gate | Threshold | Tool |
|------|-----------|------|
| Unit Test Coverage | > 80% | JaCoCo / Jest |
| Integration Tests | 100% pass | Testcontainers |
| Code Quality | A rating | SonarQube |
| Security Vulnerabilities | 0 Critical/High | Snyk / Dependabot |
| Container Vulnerabilities | 0 Critical | Trivy |

## Key Design Patterns

- **Repository Pattern**: Data access abstraction with JPA
- **Strategy Pattern**: Pluggable payment processors (`PaymentStrategy` interface)
- **Saga Pattern**: Distributed transaction coordination for order workflow
- **CQRS**: Separate read/write models for product catalog
- **Circuit Breaker**: Resilience4j for fault tolerance

CLAUDE CODE既职责: product owner同code reviewer