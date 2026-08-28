# User Payment Microservices

A microservices-based backend application built using Java and Spring Boot. This project demonstrates communication between multiple independent services using Spring Cloud and follows a distributed microservices architecture.

## Architecture

The project currently consists of the following services:

- Config Server
- Eureka Server
- API Gateway
- User Service
- Payment Service

### Architecture Flow

```text
                    Config Repository
                           |
                           v
                    +--------------+
                    | Config Server|
                    +--------------+
                           |
          --------------------------------
          |              |               |
          v              v               v
    User Service   Payment Service   API Gateway
          |              |
          ----------------
                  |
                  v
            Eureka Server

```
## Services

### Config Server

The Config Server provides centralized configuration management for all microservices.

Responsibilities:

- Centralized configuration
- Externalized application properties
- Configuration management for multiple services

### Eureka Server

Eureka Server is used for service discovery in the microservices architecture.

Responsibilities:

- Registers microservices
- Helps services discover each other
- Maintains information about available services

### API Gateway

The API Gateway acts as the main entry point for client requests.

Responsibilities:

- Routes requests to the appropriate microservices
- Provides a centralized entry point
- Integrates with Eureka Service Discovery

### User Service

The User Service handles user-related operations.

Responsibilities:

- Manage user information
- Provide user details to other microservices
- Handle user-related exceptions

### Payment Service

The Payment Service handles payment and transaction-related operations.

Responsibilities:

- Retrieve payment details
- Process transactions
- Retrieve transaction information
- Communicate with User Service using OpenFeign
- Handle exceptions
- Validate requests
- Support idempotent transactions
- Implement resilience patterns


## Technologies Used

### Programming Language
- Java

### Framework
- Spring Boot

### Spring Cloud
- Spring Cloud Config
- Netflix Eureka
- Spring Cloud Gateway
- Spring Cloud OpenFeign

### Resilience
- Resilience4j
- Circuit Breaker
- Retry
- Bulkhead
- Fallback Mechanism

### Validation
- Jakarta Bean Validation

### Build Tool
- Maven

### Other Technologies and Tools
- REST APIs
- Git
- GitHub
- Lombok
- Eclipse IDE
- Postman
