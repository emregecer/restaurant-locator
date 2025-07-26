package de.boniel.apps.restaurantlocator.mapper;

import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.fault.ErrorType;
import de.boniel.apps.restaurantlocator.model.Location;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocationMapperTest {

    private final LocationMapper mapper = LocationMapper.INSTANCE;

    @Test
    void shouldMapValidCoordinatesToLocation() {
        UUID id = UUID.randomUUID();

        LocationDto request = LocationDto.builder()
                .name("Test Restaurant")
                .type("Restaurant")
                .openingHours("09:00AM-10:00PM")
                .image("http://image")
                .coordinates("x=10,y=20")
                .radius(5)
                .build();

        Location location = mapper.mapToLocation(id, request);

        assertEquals(id, location.getId());
        assertEquals("Test Restaurant", location.getName());
        assertEquals("Restaurant", location.getType());
        assertEquals("09:00AM-10:00PM", location.getOpeningHours());
        assertEquals("http://image", location.getImage());
        assertEquals(10, location.getXCoordinate());
        assertEquals(20, location.getYCoordinate());
        assertEquals(5, location.getRadius());
    }

    @Test
    void shouldThrowApiExceptionForInvalidCoordinateFormat() {
        UUID id = UUID.randomUUID();

        LocationDto request = LocationDto.builder()
                .name("Invalid Location")
                .type("Restaurant")
                .openingHours("09:00AM-10:00PM")
                .image("http://image")
                .coordinates("10,20")
                .radius(5)
                .build();

        ApiException ex = assertThrows(ApiException.class,
                () -> mapper.mapToLocation(id, request)
        );

        assertEquals(ErrorType.INVALID_COORDINATE, ex.getErrorType());
    }

    @Test
    void shouldThrowApiExceptionForNegativeCoordinates() {
        UUID id = UUID.randomUUID();

        LocationDto request = LocationDto.builder()
                .name("Negative Location")
                .type("Restaurant")
                .openingHours("09:00AM-10:00PM")
                .image("http://image")
                .coordinates("x=-5,y=7")
                .radius(5)
                .build();

        ApiException ex = assertThrows(ApiException.class,
                () -> mapper.mapToLocation(id, request)
        );

        assertEquals(ErrorType.INVALID_COORDINATE, ex.getErrorType());
    }

    @Test
    void shouldMapLocationToDtoWithCoordinates() {
        Location location = sampleLocation();

        LocationDto dto = mapper.mapToLocationDto(location);

        assertEquals(location.getId(), dto.getId());
        assertEquals(location.getName(), dto.getName());
        assertEquals(location.getType(), dto.getType());
        assertEquals(location.getOpeningHours(), dto.getOpeningHours());
        assertEquals(location.getImage(), dto.getImage());

        assertEquals("x=10,y=20", dto.getCoordinates());
    }

    private Location sampleLocation() {
        return Location.builder()
                .id(UUID.randomUUID())
                .name("Test Place")
                .type("Restaurant")
                .xCoordinate(10)
                .yCoordinate(20)
                .radius(5)
                .openingHours("09:00-22:00")
                .image("http://image")
                .build();
    }

}
