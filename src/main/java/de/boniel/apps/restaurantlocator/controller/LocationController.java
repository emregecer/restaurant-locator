package de.boniel.apps.restaurantlocator.controller;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    //TODO: To be removed in v2
    @Operation(summary = "getAllLocations")
    @GetMapping("/v1/locations")
    public List<LocationDto> getLocations() {
        return locationService.findAll();
    }

    @Operation(summary = "searchNearbyLocations", description = "Search locations within a radius of the locations")
    @GetMapping("/v1/locations/search")
    public LocationSearchResponseDto searchNearbyLocations(@RequestParam int x, @RequestParam int y) {
        return locationService.searchNearbyLocations(new Coordinates(x, y));
    }

    @Operation(summary = "getLocationById")
    @GetMapping("/v1/locations/{id}")
    public LocationDto getLocationById(@PathVariable UUID id) {
        return locationService.getLocationById(id);
    }

    @Operation(summary = "Insert or update a location")
    @PutMapping("/v1/locations/{id}")
    public LocationDto upsertLocation(@PathVariable UUID id, @Valid @RequestBody LocationDto request) {
        return locationService.upsertLocation(id, request);
    }

}
