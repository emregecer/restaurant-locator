package de.boniel.apps.restaurantlocator.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationDataLoader implements ApplicationRunner {

    private final LocationService locationService;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Loading initial restaurant data...");

        try (InputStream inputStream = loadResource()) {
            if (inputStream == null) {
                log.warn("No restaurants.json found in resources!");
                return;
            }

            List<LocationDto> locations = Arrays.asList(
                    objectMapper.readValue(inputStream, LocationDto[].class)
            );

            locations.forEach(location -> {
                try {
                    locationService.upsertLocation(UUID.randomUUID(), location);
                    log.info("Location {} is loaded successfully. ID: {}", location.getName(), location.getId());
                } catch (Exception ex) {
                    log.error("Skipping invalid location {} -> {}", location.getName(), ex.getMessage());
                }
            });
        }

        log.info("Loaded {} locations. Data Loader completed", locationService.countLocations());
    }

    protected InputStream loadResource() {
        return getClass().getResourceAsStream("/restaurants.json"); //Might be given from outside (properties, etc.)
    }
}
