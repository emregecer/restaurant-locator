# restaurant-locator

A Spring Boot application to locate nearby restaurants

## Getting Started
- You can build the application jar with `./mvnw clean package` and then run it with `docker-compose up --build`.
- During the application startup, src/main/resources/restaurant.json will be loaded from the classpath into Postgres database.
- Once the application is running, you can access the API documentation at http://localhost:8080/swagger-ui/index.html or http://localhost:8080/v3/api-docs.

## Technical Stack
- Java 24
- Maven 3+
- Spring Boot 3.5.4
- Postgis15 (PostgreSQL15 with spatial index) for persistence and spatial-indexed location coordinates
- Spring Data JPA for data repository layer
- Hibernate-spatial for ORM (for geometrical mappings)
- Spring Validator for validating incoming requests
- Lombok and MapStruct for reducing boilerplate code
- Junit 5, Mockito, AssertJ and TestContainers for unit and integration testing
- Swagger/OpenAPI 3.0 for API documentation
- Docker for containerization

## Technical Notes
- The application is structured to support two Git branches:
  - **master**: Uses an in-memory repository for simplicity.
  - **spatial-index**: Uses a PostgreSQL database with a spatial index for efficient querying of locations based on coordinates.


- This version of the application on the **spatial-index** branch will use Postgis / PostgreSQL for storing locations. 

  - O(1) for getting locations by their ids (direct access via UUID primary keys)
  - For searching nearby locations:
    - The query uses ST_DWithin() with a spatial index, which allows the database to quickly find all locations within the radius. This operation is approximately O(log n) on indexed spatial data. 
    - The ORDER BY distance ASC is performed by the database, which sorts the filtered results. Sorting complexity is O(m log m) where m is the number of locations within the radius (typically much smaller than n). 
    - Overall, this approach offloads the heavy computation and sorting to the database, leading to significantly better performance compared to in-memory filtering and sorting of all locations (O(n log n)).

- For single responsibility and separation of concerns, the application is structured into layers:
  - Controller: Handles HTTP requests and responses.
  - Service: Contains business logic.
  - Repository: Manages data access. Contaibs JPA repositories
  - Mapper: Converts between model and DTO objects to separate data representation from core model.
  - Bootstrap: Contains LocationDataLoader to load initial data from JSON file into the in-memory repository.
  - Model: Represents the data structure of the application.
  - DTO: Data Transfer Objects for transferring data between layers.
  - Configuration: Contains application configuration settings.
  - Fault: Error related classes for handling exceptions and errors.
  - Format: Contains classes for formatting data, such as Double serialization.
  - Test: Contains unit and integration tests.
  - Utils: Contains utility classes for common operations.
  
- Added version information to URL request mappings (i.e. /v1/locations ) to allow future API versioning without breaking changes.

- The PUT endpoint to insert/update location takes the id from the URL path as the resource identifier.

## Future Improvement Ideas
- Currently, the API allows multiple locations at the same coordinates. No validation is being applied for it. 
A future enhancement could enforce uniqueness on the locations if the business rules require it (e.g., one location/restaurant per spot).
- Location types are currently in free text format. A future enhancement could introduce an enum for location types to ensure consistency and avoid typos.
- Pagination can be essential for very large datasets but since it is not in the requirements, no pagination logic has been applied. (Spring Data's Pageable can be used)
- messages.properties can be used to support internationalization (i18n) and avoid hardcoding for error messages and other user-facing strings.

## Additional Notes
- "boniel" is intentionally misspelled on packages due to search engine / security reasons.