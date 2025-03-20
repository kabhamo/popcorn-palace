# Popcorn Palace - Movie Ticket Booking System By Mohamed Kabha

## Project Overview
This document provides step-by-step instructions on setting up, building, and running the **Popcorn Palace** backend service.

## Prerequisites
Before starting, ensure you have the following installed:

- Docker (for running PostgreSQL)
- Java 17+ (for Spring Boot)
- Maven (for building the project)
- Git (for cloning the repository)

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

## Running Tests
To run unit and integration tests:
```bash
mvn test
```
