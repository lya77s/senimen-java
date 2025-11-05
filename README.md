# Senimen — Volunteer Platform

This project contains a Spring Boot (Java 17) backend with MongoDB and a vanilla HTML/CSS/JS frontend.

## Quick Start

- Requirements: Java 17+, Maven, MongoDB running locally (mongodb://localhost:27017)
- Start MongoDB and then run backend:

```bash
mvn -f backend/pom.xml spring-boot:run
```

- Open frontend using a simple server (or via Spring static serving):
  - Option 1: Use Live Server or any static server at http://127.0.0.1:5500
  - Option 2: Access via Spring at http://localhost:8080/frontend/index.html

## Environment

- Configure Mongo: `MONGODB_URI` env var
- JWT secret: `JWT_SECRET`

## CORS

Allowed origins: http://127.0.0.1:5500 and http://localhost:5500

## Seeding

Hit `POST http://localhost:8080/api/seed` to insert sample users, events, applications.

## Security toggle (dev only)

To disable security for local troubleshooting, set environment variable `SPRING_PROFILES_ACTIVE=dev-open` and adjust SecurityConfig if needed.

## Sample cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register -H 'Content-Type: application/json' -d '{"name":"Vol","email":"v@example.com","password":"pass","role":"volunteer"}'

# Login
curl -X POST http://localhost:8080/api/auth/login -H 'Content-Type: application/json' -d '{"email":"v@example.com","password":"pass"}'

# List events
curl http://localhost:8080/api/events
```

## Project structure

See directories `backend/` and `frontend/`.
# senimen-java
