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


Services
Config Server

The Config Server provides centralized configuration management for all microservices.

Responsibilities:

- Centralized configuration
- Externalized application properties
- Configuration management for multiple services
