# GitMetrics

GitMetrics is a Spring Boot REST API for tracking GitHub repository metrics over time. It retrieves repository statistics, calculates a health score, stores timestamped snapshots, and provides authenticated watchlists and dashboard analysis.

## Features

- User registration with BCrypt password hashing
- Stateless JWT authentication
- GitHub repository tracking
- Repository health-score calculation
- Historical metric snapshots
- Growth trend calculation using recent snapshots
- User-specific repository watchlists
- User dashboard responses
- Hourly synchronization of tracked repositories
- Docker packaging

## Technology Stack

- Java 21
- Spring Boot 3.5.4
- Spring Web and REST
- Spring Security
- JJWT 0.11.5
- Spring Data JPA and Hibernate
- PostgreSQL driver
- Maven
- Docker

## Architecture

The application uses a layered architecture:

```text
REST Client
    |
    v
Spring Security / JWT Filter
    |
    v
Controllers
    |
    v
Services
    |--------------------> GitHub REST API
    |
    v
Spring Data Repositories
    |
    v
PostgreSQL
```

Important packages:

- `controller`: HTTP endpoints
- `service`: application and business logic
- `repo`: Spring Data JPA repositories
- `model`: JPA entities and security principal
- `dto`: GitHub and analysis response objects
- `config`: application and security configuration
- `exception`: REST exception handling

## Main Endpoints

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| `POST` | `/github/users/register` | Register a user | Not required |
| `POST` | `/github/users/login` | Authenticate and receive a JWT | Not required |
| `POST` | `/github/track?url=...` | Fetch and store repository metrics | Required |
| `GET` | `/github/analysis?url=...` | Retrieve repository analysis | Required |
| `POST` | `/github/users/watchlist?url=...` | Add a repository to the authenticated user's watchlist | Required |
| `GET` | `/github/users/dashboard` | Retrieve the authenticated user's watchlist analysis | Required |

## Authentication

Registration and login are public. All other endpoints require a bearer token:

```text
Authorization: Bearer <JWT>
```

The JWT contains the user's email as its subject. The security filter validates the token and loads the user through `UserRepo` before placing the authenticated identity in Spring Security's `SecurityContext`.

## Health Score

The current health score is calculated by `AnalysisService`:

```text
(stars + 2 * forks) / (open issues + 1)
```

Growth is calculated by comparing the health scores of the two newest snapshots. If fewer than two snapshots exist, the API reports that there is not enough data to calculate growth.

## GitHub Integration

GitMetrics uses Spring's `RestClient` with the following base URL:

```text
https://api.github.com
```

The application calls:

```text
GET /repos/{owner}/{repo}
```

The response is mapped to `GitHubResponse`, which reads the repository name, owner login, stars, forks, and open issues.

## Data Model

- `User`: application user and watchlist owner
- `github`: tracked GitHub repository
- `snap`: timestamped repository metric snapshot
- `user_watchlists`: many-to-many relationship between users and repositories

A repository can have many snapshots, and a user can watch multiple repositories.

## Configuration

The application reads configuration from `src/main/resources/application.properties`.

Important settings include:

- `server.port`: defaults to `8080`, or uses the `PORT` environment variable
- `jwt.secret`: should be supplied through the `JWT_SECRET` environment variable
- PostgreSQL connection settings: provide these through the deployment environment
- `spring.jpa.hibernate.ddl-auto=update`: updates the schema automatically for the current development configuration

Do not commit database passwords, JWT secrets, or other credentials.

## Running Locally

### Prerequisites

- Java 21
- Maven, or the included Maven Wrapper
- PostgreSQL configured for the application

### Start the application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The server runs on:

```text
http://localhost:8080
```

### Build the project

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
```

## Docker

The multi-stage `Dockerfile`:

1. Uses Maven with Java 21 to build the application.
2. Creates the Spring Boot JAR.
3. Copies the JAR into a Java 21 runtime image.
4. Exposes port `8080`.
5. Starts the application with `java -jar app.jar`.

Build and run:

```bash
docker build -t gitmetrics .
docker run --rm -p 8080:8080 -e JWT_SECRET=<secret> gitmetrics
```

Database connection settings must also be provided when running the container.

## Testing

The repository currently contains a Spring context-load test. Unit tests for analysis, authentication, controllers, repositories, GitHub failures, and scheduled synchronization are recommended as the project grows.

Run tests with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Current Limitations

- No frontend application is included.
- GitHub OAuth or personal access token integration is not implemented.
- GitHub calls are synchronous and have no visible retry or circuit-breaker policy.
- Input validation is limited; repository URLs should be validated before production use.
- The hourly scheduler is in-process and requires coordination if multiple application instances run.
- No Azure or other cloud deployment configuration is included.
- Vector search, embeddings, and `pgvector` are not used.

## Project Structure

```text
src/
├── main/
│   ├── java/com/example/GitMetrics/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repo/
│   │   └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/GitMetrics/
```
