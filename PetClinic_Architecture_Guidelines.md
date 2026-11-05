# PetClinic Architecture Guidelines

## 1. General Coding Standards

- Follow clean code principles and maintain consistent coding practices.
- Use meaningful and descriptive names for classes, methods, variables, and packages.
- Follow Spring Boot development best practices.
- Maintain a clear separation of concerns between Controllers, Services, and Repositories.
- Write readable, reusable, and maintainable code.

## 2. Database and Entity Standards

- All primary key fields must use the Java `Long` data type.
- Use appropriate JPA annotations for entity mapping.
- Maintain proper entity relationships using appropriate JPA relationship annotations.
- Follow consistent database naming conventions.
- Ensure entity fields accurately represent the corresponding database columns.

## 3. Date and Time Standards

- Always use Java `Instant` for timestamp fields.
- Avoid using legacy `Date` and `Calendar` classes for new timestamp implementations.
- Store and manage timestamps consistently in UTC.
- Use appropriate JPA column mappings for timestamp fields.

## 4. Soft Delete Standards

- Implement soft-delete functionality using Hibernate's `@SQLDelete` annotation.
- Records must not be physically deleted from the database during normal delete operations.
- Ensure soft-deleted records are excluded from normal retrieval operations where appropriate.
- Maintain a consistent deletion-status field across entities that support soft deletion.
- Ensure repository queries respect soft-delete behavior.

## 5. Repository Standards

- Use Spring Data JPA for database access.
- Repository interfaces should extend appropriate Spring Data repository interfaces, such as `Repository`, `CrudRepository`, or `JpaRepository`.
- Prefer standard Spring Data repository methods whenever possible.
- Use custom JPQL or native SQL queries only when necessary.
- Define custom queries using the `@Query` annotation where appropriate.
- Keep database access logic inside repository interfaces.
- Avoid implementing business logic inside repository classes or interfaces.

## 6. Service Layer Standards

- Implement business logic in the Service layer.
- Controllers must delegate business operations to the appropriate Service classes.
- Use Spring's `@Service` annotation for service components.
- Apply appropriate transaction management using `@Transactional`.
- Keep services focused on specific business responsibilities.
- Handle business exceptions consistently.

## 7. REST API Standards

- Follow RESTful API design principles.
- Use appropriate HTTP methods, including `GET`, `POST`, `PUT`, `PATCH`, and `DELETE`.
- Return appropriate HTTP status codes.
- Use consistent request and response structures.
- Keep API documentation updated using OpenAPI specifications.
- Validate incoming request data.
- Handle API errors using consistent exception-handling mechanisms.

## 8. Maintainability and Testing

- Write reusable, modular, and testable code.
- Follow consistent exception-handling practices.
- Write unit tests for business logic and service methods.
- Include repository tests for database operations.
- Maintain adequate test coverage for critical functionality.
- Avoid unnecessary code duplication.
- Refactor code when necessary to improve readability and maintainability.

## 9. Architecture Compliance Rules

All new code and modifications to existing code should follow these mandatory conventions:

| Area | Mandatory Convention |
| --- | --- |
| Primary Keys | Use Java `Long` |
| Timestamps | Use Java `Instant` |
| Timestamp Storage | Use UTC |
| Soft Delete | Use Hibernate `@SQLDelete` |
| Data Access | Use Spring Data JPA |
| Business Logic | Keep in the Service layer |
| API Documentation | Maintain OpenAPI specifications |
| Testing | Include appropriate unit and repository tests |

> **Important:** When reviewing existing code, identify violations of these guidelines and report the exact Java file paths, affected classes, and recommended corrections.

When generating new code, follow these architecture guidelines by default.