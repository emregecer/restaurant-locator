package de.boniel.apps.restaurantlocator.mapper;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResultDto;
import de.boniel.apps.restaurantlocator.model.Location;
import de.boniel.apps.restaurantlocator.model.LocationWithDistance;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocationMapperTest {

    private final LocationMapper mapper = LocationMapper.INSTANCE;

    private final GeometryFactory geometryFactory = new GeometryFactory();

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
        assertEquals(10, location.getCoordinates().getX());
        assertEquals(20, location.getCoordinates().getY());
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

        LocationWithDistance locationWithDistance = mock(LocationWithDistance.class);

        when(locationWithDistance.getId()).thenReturn(UUID.randomUUID());
        when(locationWithDistance.getName()).thenReturn("Deseado Steakhaus");
        when(locationWithDistance.getX()).thenReturn(2D);
        when(locationWithDistance.getY()).thenReturn(2D);
        when(locationWithDistance.getDistance()).thenReturn(5D);

        List<LocationWithDistance> locations = List.of(locationWithDistance);

        LocationSearchResponseDto response = mapper.mapToLocationSearchResponseDto(userCoordinates, locations);

        assertThat(response).isNotNull();
        assertThat(response.getUserLocation()).isEqualTo(userCoordinates);

        assertThat(response.getLocations()).hasSize(1);

        LocationSearchResultDto resultDto = response.getLocations().getFirst();
        assertThat(resultDto.getId()).isEqualTo(locationWithDistance.getId());
        assertThat(resultDto.getName()).isEqualTo(locationWithDistance.getName());
        assertThat(resultDto.getCoordinates().getX()).isEqualTo(2);
        assertThat(resultDto.getCoordinates().getY()).isEqualTo(2);

        assertThat(resultDto.getDistance()).isEqualTo(5.0);
    }

    @Test
    void shouldMapToMultipleLocationsWithDistances() {
        Coordinates userCoordinates = new Coordinates(3, 2);

        LocationWithDistance loc1 = mock(LocationWithDistance.class);
        when(loc1.getId()).thenReturn(UUID.randomUUID());
        when(loc1.getName()).thenReturn("Loc1");
        when(loc1.getX()).thenReturn(2.0);
        when(loc1.getY()).thenReturn(2.0);
        when(loc1.getDistance()).thenReturn(1.5);

        LocationWithDistance loc2 = mock(LocationWithDistance.class);
        when(loc2.getId()).thenReturn(UUID.randomUUID());
        when(loc2.getName()).thenReturn("Loc2");
        when(loc2.getX()).thenReturn(2.0);
        when(loc2.getY()).thenReturn(3.0);
        when(loc2.getDistance()).thenReturn(2.5);

        List<LocationWithDistance> mockLocations = List.of(loc1, loc2);

        LocationSearchResponseDto response = mapper.mapToLocationSearchResponseDto(userCoordinates, mockLocations);

        assertThat(response.getUserLocation()).isEqualTo(userCoordinates);
        assertThat(response.getLocations()).hasSize(2);

        LocationSearchResultDto dto1 = response.getLocations().get(0);
        LocationSearchResultDto dto2 = response.getLocations().get(1);

        assertThat(dto1.getDistance()).isEqualTo(1.5);
        assertThat(dto2.getDistance()).isEqualTo(2.5);
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
                .coordinates(geometryFactory.createPoint(new Coordinate(10, 20)))
                .radius(5)
                .openingHours("09:00-22:00")
                .image("http://image")
                .build();
    }
}
