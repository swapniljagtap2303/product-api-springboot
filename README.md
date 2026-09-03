<<<<<<< HEAD
# Zest India – Product REST API

Implementation of the supplied Java Backend Developer Technical Evaluation. The assignment asks for Product CRUD, Java 17+, Spring Boot, JPA/Hibernate, PostgreSQL/MySQL, JWT + refresh token rotation, RBAC, validation, indexing, async where applicable, CORS/HTTPS, JUnit/Mockito, H2 integration testing, Swagger/OpenAPI and Docker. Evaluation focuses on clean architecture, REST quality, security, tests, documentation and deployment readiness.

## Architecture
Controller -> Service -> Repository -> MySQL 8.0. DTOs keep API contracts separate from entities. Security uses stateless JWT authentication. Refresh tokens are stored and rotated (old token is revoked on refresh). GlobalExceptionHandler provides a standard error response.

## API
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/auth/refresh?refreshToken=...
GET /api/v1/products?page=0&size=10
GET /api/v1/products/{id}
POST /api/v1/products
PUT /api/v1/products/{id}
DELETE /api/v1/products/{id}
GET /api/v1/products/{id}/items

Product APIs require a Bearer access token and USER/ADMIN role. Pagination is enabled on GET collection.

## Run
Requirements: Java 17+, Maven 3.9+, MySQL 8.0.
Set DB_URL, DB_USERNAME, DB_PASSWORD and JWT_SECRET. Run `mvn spring-boot:run`.
Swagger: http://localhost:8080/swagger-ui.html
Tests: `mvn test`
Docker: `docker compose up --build`

## Security
Passwords use BCrypt. Access JWT expires after 15 minutes by default. Refresh tokens expire after 7 days and are rotated/revoked. CORS is configured. In production, terminate TLS at a reverse proxy/load balancer and set X-Forwarded-Proto correctly.

## Database
Product and Item tables follow the assignment model. Indexes are added to product_name and item.product_id for lookup/join performance.

## Testing
JUnit 5 + Mockito unit tests cover service/controller behavior. Spring Boot integration test uses H2 via application-test.yml.

## Note on async
The supplied assignment says async processing where applicable; the CRUD path is synchronous because it requires immediate API responses and no long-running background operation is required by the stated endpoints. This avoids unnecessary complexity.
=======
# product-api-springboot
Product REST API using Spring Boot, JPA, MySQL and JWT
>>>>>>> 85e76071421575a516d31349be3cfd752d3cab17
