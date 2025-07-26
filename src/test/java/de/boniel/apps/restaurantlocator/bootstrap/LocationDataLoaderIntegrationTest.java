package de.boniel.apps.restaurantlocator.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.ApplicationArguments;

import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LocationDataLoaderIntegrationTest {

    private LocationService locationService;
    private LocationDataLoader loader;

    @BeforeEach
    void setUp() {
        locationService = mock(LocationService.class);
        loader = new LocationDataLoader(locationService, new ObjectMapper());
    }

    @Test
    void shouldLoadLocationsFromJsonAndCallService() throws Exception {
        // Will read restaurants.json from resources
        LocationDataLoader spyLoader = spy(loader);

        try (InputStream ignored = spyLoader.loadResource()) {
            spyLoader.run(mock(ApplicationArguments.class));

            ArgumentCaptor<LocationDto> captor = ArgumentCaptor.forClass(LocationDto.class);
            verify(locationService, times(21)).upsertLocation(any(UUID.class), captor.capture());

            LocationDto captured = captor.getValue(); //Will capture the last location processed
            assertEquals("Kebab Kingdom", captured.getName());
            assertEquals("Restaurant", captured.getType());
            assertEquals(7, captured.getCoordinates().getX());
            assertEquals(3, captured.getCoordinates().getY());
            assertEquals(7, captured.getRadius());
        }
    }
}
