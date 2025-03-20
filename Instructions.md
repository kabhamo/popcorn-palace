# Popcorn Palace - Movie Ticket Booking System

## Project Overview
This document provides step-by-step instructions on setting up, building, and running the **Popcorn Palace** backend service.

## Prerequisites
Before starting, ensure you have the following installed:

- [Docker](https://www.docker.com/get-started) (for running PostgreSQL)
- [Java 17+](https://adoptium.net/) (for Spring Boot)
- [Maven](https://maven.apache.org/) (for building the project)
- [Git](https://git-scm.com/) (for cloning the repository)
- [Postman](https://www.postman.com/) (Optional, for testing APIs)

## Setup & Running the Service

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/popcorn-palace.git
cd popcorn-palace
```

### 2. Start PostgreSQL with Docker Compose
The `compose.yml` file sets up a **PostgreSQL** database.
```bash
docker-compose up -d
```

### 4. Build the Application
Run the following command to compile the project:
```bash
mvn clean package
```

### 5. Run the Spring Boot Application
Once the database is running, start the backend service:
```bash
mvn spring-boot:run
```
OR (Run the JAR directly)
```bash
java -jar target/popcorn-palace-0.0.1-SNAPSHOT.jar
```

## Testing the API
Once the application is running, the backend will be available at:
```
http://localhost:8080
```

### Example API Endpoints
| Action | HTTP Method | Endpoint |
|--------|------------|----------|
| Fetch all movies | `GET` | `/api/movies` |
| Add a new movie | `POST` | `/api/movies` |
| Update a movie | `PUT` | `/api/movies/{id}` |
| Delete a movie | `DELETE` | `/api/movies/{id}` |
| Fetch showtime by ID | `GET` | `/api/showtimes/{id}` |
| Book a ticket | `POST` | `/api/tickets` |

Test using **Postman** or `curl`.

Example:
```bash
curl -X GET http://localhost:8080/api/movies
```

## Running Tests
To run unit and integration tests:
```bash
mvn test
```

## Stopping the Services
### Stop PostgreSQL Container:
```bash
docker-compose down
```
