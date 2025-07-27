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

import static java.util.Optional.ofNullable;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    GeometryFactory geometryFactory = new GeometryFactory();

    @Mapping(target = "id", source = "id")
    @Mapping(target = "coordinates", source = "request.coordinates", qualifiedByName = "mapToPoint")
    Location mapToLocation(UUID id, LocationDto request);

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

    @Mapping(target = "coordinates.x", source = "location.x")
    @Mapping(target = "coordinates.y", source = "location.y")
    @Mapping(target = "distance", source = "location.distance")
    LocationSearchResultDto mapToLocationSearchResponseDto(LocationWithDistance location, @Context Coordinates userCoordinates);

    @Named("mapToPoint")
    default Point mapToPoint(Coordinates userCoordinates) {
        return ofNullable(userCoordinates)
                .map(coordinates -> geometryFactory.createPoint(new Coordinate(coordinates.getX(), coordinates.getY())))
                .orElse(null);
    }
}
