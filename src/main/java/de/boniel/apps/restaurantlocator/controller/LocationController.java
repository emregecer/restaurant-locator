package de.boniel.apps.restaurantlocator.controller;

import de.boniel.apps.restaurantlocator.dto.LocationDto;
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

    @Operation(summary = "getLocationById")
    @GetMapping("/v1/locations/{id}")
    public LocationDto getLocationById(@PathVariable UUID id) {
        return locationService.getLocationById(id);
    }

    @Operation(summary = "Insert or update a location")
    @PutMapping("/v1/locations/{id}")
    public void upsertLocation(@PathVariable UUID id, @Valid @RequestBody LocationDto request) {
        locationService.upsertLocation(id, request);
    }

}
