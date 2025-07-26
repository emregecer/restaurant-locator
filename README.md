# restaurant-locator

A Spring Boot application to locate restaurants

## Getting Started
During the application startup, locations.json is loaded from the classpath into an in-memory repository.

## Technical Stack
- Java 24
- Spring Boot 3.5.4
- Lombok and MapStruct for reducing boilerplate code

## Technical Notes
- This version of the application will use an in-memory, non-persistent repository for storing locations (ConcurrentHashMap<UUID, Location>) for simplicity and avoid complexity. 
  - O(1) for getting locations by their ids.
  - O(n) for searching locations by their distances from a given point which is fine for 100K locations.

- For single responsibility and separation of concerns, the application is structured into layers:
  - Validation: Contains validation logic for incoming requests.  
  - Controller: Handles HTTP requests and responses.
  - Service: Contains business logic.
  - Repository: Manages data access. Used an in-memory repository for simplicity for now
  - Mapper: Converts between model and DTO objects.
  - Model: Represents the data structure of the application.
  - Configuration: Contains application configuration settings.
  - Fault: Handles error responses and exceptions
  - Test: Contains unit and integration tests.
  - Utils: Contains utility classes for common operations.
  
- Added version information to URL request mappings (i.e. /v1/locations ) to allow future API versioning without breaking changes.

- The PUT endpoint to insert/update location takes the id from the URL path as the resource identifier. 
The request body excludes id to avoid redundancy.

## Future Improvement Ideas
- Currently, the API allows multiple locations at the same coordinates. No validation is being applied for it. A future enhancement could enforce uniqueness on the locations if the business rules require it (e.g., one location/restaurant per spot).


## Additional Notes
- "boniel" is intentionally misspelled on packages due to search engine / security reasons.
- 