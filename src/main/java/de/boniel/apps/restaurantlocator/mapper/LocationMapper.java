package de.boniel.apps.restaurantlocator.mapper;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResultDto;
import de.boniel.apps.restaurantlocator.model.Location;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", source = "id") // Uses "id" parameter. Ignores request.id
    @Mapping(target = "xCoordinate", source = "request.coordinates.x")
    @Mapping(target = "yCoordinate", source = "request.coordinates.y")
    Location mapToLocation(UUID id, LocationDto request);

    @Mapping(target = "coordinates.x", source = "XCoordinate")
    @Mapping(target = "coordinates.y", source = "YCoordinate")
    LocationDto mapToLocationDto(Location location);

    default LocationSearchResponseDto mapToLocationSearchResponseDto(Coordinates userCoordinates,
                                                                     List<Location> locations) {
        return LocationSearchResponseDto.builder()
                .userLocation(userCoordinates)
                .locations(locations.stream()
                        .map(location -> mapToLocationSearchResponseDto(location, userCoordinates))
                        .toList()
                )
                .build();
    }

    @Mapping(target = "coordinates.x", source = "XCoordinate")
    @Mapping(target = "coordinates.y", source = "YCoordinate")
    @Mapping(target = "distance", source = "location", qualifiedByName = "mapDistance")
    LocationSearchResultDto mapToLocationSearchResponseDto(Location location,
                                                           @Context Coordinates userCoordinates);

    @Named("mapDistance")
    default double mapDistance(Location location, @Context Coordinates userCoordinates) {
        return Math.sqrt(location.calculateDistance(userCoordinates));
    }

}
