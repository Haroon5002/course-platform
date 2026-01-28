# Course Platform API

A comprehensive backend service for a learning platform built with Spring Boot, enabling users to browse courses, enroll, and track their learning progress.

## Features

- **Authentication**: JWT-based user registration and login
- **Course Browsing**: Public access to course catalog with detailed content
- **Advanced Search**: Case-insensitive, partial-match search across courses, topics, and subtopics
- **Enrollment Management**: Prevent duplicate enrollments with proper validation
- **Progress Tracking**: Mark subtopics as completed and view learning progress
- **API Documentation**: Interactive Swagger UI for easy API testing

## Tech Stack

- **Java**: 17+
- **Spring Boot**: 3.2.1
- **Spring Security**: JWT authentication
- **PostgreSQL**: Database
- **JPA/Hibernate**: ORM
- **Swagger/OpenAPI**: API documentation
- **Maven**: Build tool

## Project Structure

```
course-platform-api/
├── src/main/
│   ├── java/com/courseplatform/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA entities
│   │   ├── exception/       # Custom exceptions
│   │   ├── repository/      # Data repositories
│   │   ├── security/        # Security configuration
│   │   └── service/         # Business logic
│   └── resources/
│       ├── application.properties
│       └── seed-data.json   # Course seed data
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Local Setup

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd course-platform-api
   ```

2. **Set up PostgreSQL database**
   ```bash
   createdb courseplatform
   ```

3. **Configure environment variables**

   Create a `.env` file or set environment variables:
   ```bash
   export DATABASE_URL=jdbc:postgresql://localhost:5432/courseplatform
   export DATABASE_USERNAME=postgres
   export DATABASE_PASSWORD=your_password
   export JWT_SECRET=your-secret-key-at-least-256-bits-long
   ```

   Or update `src/main/resources/application.properties` directly.

4. **Build the application**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`

6. **Access Swagger UI**
   ```
   http://localhost:8080/swagger-ui.html
   ```

## API Endpoints

### Authentication (Public)

#### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "student@example.com",
  "password": "securePassword123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "student@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "student@example.com",
  "expiresIn": 86400
}
```

### Courses (Public)

#### List All Courses
```http
GET /api/courses
```

#### Get Course Details
```http
GET /api/courses/{courseId}
```

### Search (Public)

#### Search Courses
```http
GET /api/search?q=velocity
```

### Enrollment (Requires Authentication)

#### Enroll in Course
```http
POST /api/courses/{courseId}/enroll
Authorization: Bearer <jwt-token>
```

### Progress Tracking (Requires Authentication)

#### Mark Subtopic Complete
```http
POST /api/subtopics/{subtopicId}/complete
Authorization: Bearer <jwt-token>
```

#### View Progress
```http
GET /api/enrollments/{enrollmentId}/progress
Authorization: Bearer <jwt-token>
```

## Using Swagger UI

1. Navigate to `http://localhost:8080/swagger-ui.html`
2. Test public endpoints immediately (no authentication needed)
3. For authenticated endpoints:
   - First register/login via `/api/auth/register` or `/api/auth/login`
   - Copy the JWT token from the response
   - Click "Authorize" button (top right)
   - Enter: `Bearer <your-token>`
   - Click "Authorize" then "Close"
   - Now you can test authenticated endpoints

## Database Schema

### Entities

- **User**: Stores user credentials
- **Course**: Course information with topics
- **Topic**: Logical grouping of subtopics within a course
- **Subtopic**: Individual learning units with markdown content
- **Enrollment**: Links users to courses
- **SubtopicProgress**: Tracks completion status

### Seed Data

On first startup, the application automatically loads:
- 2 courses (Physics 101, Math 101)
- 6 topics (3 per course)
- 18 subtopics with detailed markdown content

## Deployment

### Environment Variables for Production

```bash
DATABASE_URL=jdbc:postgresql://<host>:<port>/<database>
DATABASE_USERNAME=<username>
DATABASE_PASSWORD=<password>
JWT_SECRET=<secure-256-bit-secret>
PORT=8080
```

### Deployment Platforms

This application can be deployed to:
- **Railway**: Supports PostgreSQL + Spring Boot
- **Render**: Free tier available
- **Fly.io**: Container-based deployment
- **Heroku**: Classic PaaS option

### Railway Deployment Example

1. Install Railway CLI or use web interface
2. Create new project and add PostgreSQL
3. Set environment variables
4. Deploy:
   ```bash
   railway up
   ```

### Docker Deployment

Create a `Dockerfile`:
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
mvn clean package
docker build -t course-platform-api .
docker run -p 8080:8080 course-platform-api
```

## Error Handling

The API returns standardized error responses:

```json
{
  "error": "Error Type",
  "message": "Detailed error message",
  "timestamp": "2025-01-28T10:30:00"
}
```

### HTTP Status Codes

- **200 OK**: Successful GET request
- **201 Created**: Successful POST request
- **400 Bad Request**: Invalid input
- **401 Unauthorized**: Missing/invalid token
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **409 Conflict**: Duplicate resource

## Testing with cURL

### Register and Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

### Search (Public)
```bash
curl http://localhost:8080/api/search?q=velocity
```

### Enroll (Authenticated)
```bash
curl -X POST http://localhost:8080/api/courses/physics-101/enroll \
  -H "Authorization: Bearer <your-token>"
```

## Development Notes

### Code Organization

- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic
- **Repositories**: Data access layer
- **DTOs**: Request/response objects
- **Entities**: Database models

### Security

- Passwords are encrypted with BCrypt
- JWT tokens expire after 24 hours (configurable)
- All authenticated endpoints require valid JWT
- CORS can be configured in `SecurityConfig`

### Search Implementation

The current search uses PostgreSQL's LIKE operator for:
- Case-insensitive matching
- Partial string matching
- Search across multiple fields

For production, consider:
- Full-text search with PostgreSQL
- Elasticsearch for advanced features
- Caching for frequent searches

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write/update tests
5. Submit a pull request

## License

MIT License - feel free to use this project for learning and development.

## Support

For questions or issues:
- Create an issue in the repository
- Check Swagger documentation at `/swagger-ui.html`
- Review error messages in application logs

## Acknowledgments

- Spring Boot Documentation
- PostgreSQL Documentation
- JWT.io for token debugging
- Swagger for API documentation
