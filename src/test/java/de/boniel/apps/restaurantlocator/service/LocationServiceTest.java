package de.boniel.apps.restaurantlocator.service;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.model.Location;
import de.boniel.apps.restaurantlocator.model.LocationWithDistance;
import de.boniel.apps.restaurantlocator.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.boniel.apps.restaurantlocator.fault.ErrorType.LOCATION_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository repository;

    @InjectMocks
    private LocationService service;

    private final UUID TEST_ID = UUID.randomUUID();

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    void shouldReturnAllLocationsAsDtos() {
        Location loc1 = sampleLocation();
        Location loc2 = loc1.toBuilder().id(UUID.randomUUID()).name("Another").build();
        when(repository.findAll()).thenReturn(List.of(loc1, loc2));

        List<LocationDto> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("Test Place", result.get(0).getName());
        assertEquals("Another", result.get(1).getName());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnOnlyNearbyLocationsSortedByDistance() {
        Coordinates userCoordinates = new Coordinates(3, 2);

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        LocationWithDistance loc1 = mock(LocationWithDistance.class);
        when(loc1.getId()).thenReturn(id1);
        when(loc1.getName()).thenReturn("Loc1");
        when(loc1.getX()).thenReturn(2.0);
        when(loc1.getY()).thenReturn(2.0);
        when(loc1.getDistance()).thenReturn(1.0);

        LocationWithDistance loc2 = mock(LocationWithDistance.class);
        when(loc2.getId()).thenReturn(id2);
        when(loc2.getName()).thenReturn("Loc2");
        when(loc2.getX()).thenReturn(2.0);
        when(loc2.getY()).thenReturn(3.0);
        when(loc2.getDistance()).thenReturn(Math.sqrt(2));

        when(repository.findLocationsWithinRadiusWithDistance(userCoordinates.getX(), userCoordinates.getY()))
                .thenReturn(List.of(loc1, loc2));

        LocationSearchResponseDto response = service.searchNearbyLocations(userCoordinates);

        assertThat(response).isNotNull();
        assertThat(response.getUserLocation()).isEqualTo(userCoordinates);
        assertThat(response.getLocations()).hasSize(2);
    }

    @Test
    void shouldReturnLocationByIdAsDto() {
        when(repository.findById(TEST_ID)).thenReturn(Optional.of(sampleLocation()));

        LocationDto dto = service.getLocationById(TEST_ID);

        assertEquals(TEST_ID, dto.getId());

        assertEquals("Test Place", dto.getName());
        verify(repository).findById(TEST_ID);
    }

    @Test
    void shouldThrowApiExceptionWhenLocationNotFound() {
        when(repository.findById(TEST_ID)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(
                ApiException.class,
                () -> service.getLocationById(TEST_ID)
        );

        assertEquals(LOCATION_NOT_FOUND, ex.getErrorType());

        verify(repository).findById(TEST_ID);
    }

    @Test
    void shouldSaveValidLocation() {
        LocationDto request = LocationDto.builder()
                .name("New Place")
                .type("Restaurant")
                .openingHours("09-21")
                .image("img")
                .coordinates(new Coordinates(15, 25))
                .radius(3)
                .build();

        service.upsertLocation(TEST_ID, request);

        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(repository).save(captor.capture());

        Location saved = captor.getValue();

        assertEquals(TEST_ID, saved.getId());
        assertEquals(15, saved.getCoordinates().getX());
        assertEquals(25, saved.getCoordinates().getY());
        assertEquals(3, saved.getRadius());
    }

    private Location sampleLocation() {
        return Location.builder()
                .id(TEST_ID)
                .name("Test Place")
                .type("Restaurant")
                .coordinates(geometryFactory.createPoint(new Coordinate(10, 20)))
                .radius(5)
                .openingHours("10-22")
                .image("http://img")
                .build();
    }
}
