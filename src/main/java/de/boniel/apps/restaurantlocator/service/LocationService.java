package de.boniel.apps.restaurantlocator.service;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.mapper.LocationMapper;
import de.boniel.apps.restaurantlocator.model.Location;
import de.boniel.apps.restaurantlocator.repository.LocationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static de.boniel.apps.restaurantlocator.fault.ErrorType.LOCATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Validated
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDto> findAll() {
        return locationRepository.findAll().stream()
                .map(LocationMapper.INSTANCE::mapToLocationDto)
                .toList();
    }

    public LocationSearchResponseDto searchNearbyLocations(Coordinates userCoordinates) {
        List<Location> nearbyLocations = locationRepository.findAll().stream()
                .filter(location -> location.calculateDistanceSquared(userCoordinates) <= location.calculateRadiusSquared())
                .sorted(Comparator.comparingDouble(location -> location.calculateDistanceSquared(userCoordinates)))
                .toList();

        return LocationMapper.INSTANCE.mapToLocationSearchResponseDto(userCoordinates, nearbyLocations);
    }

    public LocationDto upsertLocation(UUID id, @Valid LocationDto request) {
        Location location = LocationMapper.INSTANCE.mapToLocation(id, request);
        Location createdLocation = locationRepository.save(location);

        return LocationMapper.INSTANCE.mapToLocationDto(createdLocation);
    }

    public LocationDto getLocationById(UUID id) {
        return locationRepository.findById(id)
                .map(LocationMapper.INSTANCE::mapToLocationDto)
                .orElseThrow(() -> new ApiException(LOCATION_NOT_FOUND, "Location not found with ID: " + id));
    }

    public int countLocations() {
        return locationRepository.count();
    }
}
