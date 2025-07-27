# restaurant-locator

A Spring Boot application to locate nearby restaurants

## Getting Started
- Just run RestaurantLocatorApplication with Java24.
- During the application startup, src/main/resources/restaurant.json will be loaded from the classpath into an in-memory repository.

## Technical Stack
- Java 24
- Spring Boot 3.5.4
- Spring Validator for validating incoming requests
- Lombok and MapStruct for reducing boilerplate code

## Technical Notes
- This version of the application on the **master** branch will use an in-memory, non-persistent repository for storing locations (ConcurrentHashMap<UUID, Location>) for simplicity and avoid complexity. 
  - O(1) for getting locations by their ids (direct access via UUID)
  - For searching nearby locations:
    - Fetches all locations from the repository and filter the locations by their distances to user (O(n))
    - Sorting k elements (where k ≤ n, the ones passing filter)
    - Sorting is O(k log k) worst case
    - Overall complexity for searching is O(n + k log k) where n is the number of locations in the repository.
- On **persistence** branch, the application uses a persistent repository (PostgreSQL) to store locations with spatial index.

- For single responsibility and separation of concerns, the application is structured into layers:
  - Controller: Handles HTTP requests and responses.
  - Service: Contains business logic.
  - Repository: Manages data access. Used an in-memory repository for simplicity for now
  - Mapper: Converts between model and DTO objects to separate data representation from core model.
  - Bootstrap: Contains 
  - Model: Represents the data structure of the application.
  - Configuration: Contains application configuration settings.
  - Fault: Error related classes for handling exceptions and errors.
  - Test: Contains unit and integration tests.
  - Utils: Contains utility classes for common operations.
  
- Added version information to URL request mappings (i.e. /v1/locations ) to allow future API versioning without breaking changes.

- The PUT endpoint to insert/update location takes the id from the URL path as the resource identifier.

## Future Improvement Ideas
- Currently, the API allows multiple locations at the same coordinates. No validation is being applied for it. A future enhancement could enforce uniqueness on the locations if the business rules require it (e.g., one location/restaurant per spot).
- Location types are currently in free text format. A future enhancement could introduce an enum for location types to ensure consistency and avoid typos.
- messages.properties can be used to support internationalization (i18n) and avoid hardcoding for error messages and other user-facing strings.

## Additional Notes
- "boniel" is intentionally misspelled on packages due to search engine / security reasons.