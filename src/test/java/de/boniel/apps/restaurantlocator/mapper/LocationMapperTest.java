package de.boniel.apps.restaurantlocator.mapper;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResultDto;
import de.boniel.apps.restaurantlocator.model.Location;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .coordinates(new Coordinates(10, 20))
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
    void shouldMapLocationToDtoWithCoordinates() {
        Location location = sampleLocation();

        LocationDto dto = mapper.mapToLocationDto(location);

        assertEquals(location.getId(), dto.getId());
        assertEquals(location.getName(), dto.getName());
        assertEquals(location.getType(), dto.getType());
        assertEquals(location.getOpeningHours(), dto.getOpeningHours());
        assertEquals(location.getImage(), dto.getImage());

        assertEquals(10, dto.getCoordinates().getX());
        assertEquals(20, dto.getCoordinates().getY());
    }

    @Test
    void shouldMapSingleLocationSearchResponseDtoCorrectly() {
        Coordinates userCoordinates = new Coordinates(3, 2);

        Location location = Location.builder()
                .id(UUID.randomUUID())
                .name("Deseado Steakhaus")
                .xCoordinate(2)
                .yCoordinate(2)
                .radius(5)
                .build();

        List<Location> locations = List.of(location);

        LocationSearchResponseDto response = mapper.mapToLocationSearchResponseDto(userCoordinates, locations);

        assertThat(response).isNotNull();
        assertThat(response.getUserLocation()).isEqualTo(userCoordinates);

        assertThat(response.getLocations()).hasSize(1);

        LocationSearchResultDto resultDto = response.getLocations().get(0);
        assertThat(resultDto.getId()).isEqualTo(location.getId());
        assertThat(resultDto.getName()).isEqualTo(location.getName());
        assertThat(resultDto.getCoordinates().getX()).isEqualTo(2);
        assertThat(resultDto.getCoordinates().getY()).isEqualTo(2);

        // Distance between (3,2) and (2,2) = sqrt((3-2)^2 + (2-2)^2) = 1.0
        assertThat(resultDto.getDistance()).isEqualTo(1.0);
    }

    @Test
    void shouldMapToMultipleLocationsWithDistances() {
        Coordinates userCoordinates = new Coordinates(3, 2);

        List<Location> locations = List.of(
                Location.builder()
                        .id(UUID.randomUUID())
                        .name("Loc1")
                        .xCoordinate(2)
                        .yCoordinate(2)
                        .radius(2)
                        .build(),
                Location.builder()
                        .id(UUID.randomUUID())
                        .name("Loc2")
                        .xCoordinate(2)
                        .yCoordinate(3)
                        .radius(5)
                        .build()
        );

        LocationSearchResponseDto response = mapper.mapToLocationSearchResponseDto(userCoordinates, locations);

        assertThat(response.getUserLocation()).isEqualTo(userCoordinates);
        assertThat(response.getLocations()).hasSize(2);

        LocationSearchResultDto dto1 = response.getLocations().get(0);
        LocationSearchResultDto dto2 = response.getLocations().get(1);

        assertThat(dto1.getDistance()).isEqualTo(1.0);
        assertThat(dto2.getDistance()).isCloseTo(1.41421, within(0.0001));
    }

    @Test
    void shouldHandleEmptyLocationsList() {
        Coordinates userCoordinates = new Coordinates(3, 2);

        LocationSearchResponseDto response = mapper.mapToLocationSearchResponseDto(userCoordinates, List.of());

        assertThat(response.getUserLocation()).isEqualTo(userCoordinates);
        assertThat(response.getLocations()).isEmpty();
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
