package de.boniel.apps.restaurantlocator.service;

import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.model.Coordinates;
import de.boniel.apps.restaurantlocator.model.Location;
import de.boniel.apps.restaurantlocator.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.boniel.apps.restaurantlocator.fault.ErrorType.LOCATION_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository repository;

    @InjectMocks
    private LocationService service;

    private final UUID TEST_ID = UUID.randomUUID();

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
        assertEquals(15, saved.getXCoordinate());
        assertEquals(25, saved.getYCoordinate());
        assertEquals(3, saved.getRadius());
    }

    private Location sampleLocation() {
        return Location.builder()
                .id(TEST_ID)
                .name("Test Place")
                .type("Restaurant")
                .xCoordinate(10)
                .yCoordinate(20)
                .radius(5)
                .openingHours("10-22")
                .image("http://img")
                .build();
    }
}
