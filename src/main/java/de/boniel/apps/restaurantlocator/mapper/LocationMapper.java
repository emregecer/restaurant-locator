package de.boniel.apps.restaurantlocator.mapper;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResultDto;
import de.boniel.apps.restaurantlocator.model.Location;
import de.boniel.apps.restaurantlocator.model.LocationWithDistance;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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

    GeometryFactory geometryFactory = new GeometryFactory();

    @Mapping(target = "id", source = "id")
    @Mapping(target = "coords", source = "request.coordinates", qualifiedByName = "mapToCoordinates")
    Location mapToLocation(UUID id, LocationDto request);

    @Mapping(target = "coordinates.x", source = "coords.x")
    @Mapping(target = "coordinates.y", source = "coords.y")
    LocationDto mapToLocationDto(Location location);

    default LocationSearchResponseDto mapToLocationSearchResponseDto(Coordinates userCoordinates,
                                                                     List<LocationWithDistance> locations) {
        return LocationSearchResponseDto.builder()
                .userLocation(userCoordinates)
                .locations(locations.stream()
                        .map(location -> mapToLocationSearchResponseDto(location, userCoordinates))
                        .toList()
                )
                .build();
    }

    @Mapping(target = "coordinates.x", source = "x")
    @Mapping(target = "coordinates.y", source = "y")
    @Mapping(target = "distance", source = "location.distance")
    LocationSearchResultDto mapToLocationSearchResponseDto(LocationWithDistance location, @Context Coordinates userCoordinates);

    @Named("mapToCoordinates")
    default Point mapToCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            return null;
        }
        return geometryFactory.createPoint(new Coordinate(coordinates.getX(), coordinates.getY()));
    }
}
