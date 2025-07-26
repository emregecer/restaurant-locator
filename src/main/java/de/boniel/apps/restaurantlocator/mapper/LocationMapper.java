package de.boniel.apps.restaurantlocator.mapper;

import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.model.Location;
import de.boniel.apps.restaurantlocator.utils.LocationUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", source = "id") // Uses "is" parameter. Ignores request.id
    @Mapping(target = "xCoordinate", source = "request.coordinates", qualifiedByName = "mapToXCoordinate")
    @Mapping(target = "yCoordinate", source = "request.coordinates", qualifiedByName = "mapToYCoordinate")
    Location mapToLocation(UUID id, LocationDto request);

    LocationDto mapToLocationDto(Location location);

    @AfterMapping
    default void afterMapToLocationDto(@MappingTarget LocationDto.LocationDtoBuilder<?, ?> locationDtoBuilder,
                                       Location location) {
        locationDtoBuilder.coordinates(
                LocationUtils.toCoordinateString(location.getXCoordinate(), location.getYCoordinate())
        );
    }

    @Named("mapToXCoordinate")
    default int mapToXCoordinate(String coordinates) {
        return LocationUtils.parseCoordinate(coordinates, true);
    }

    @Named("mapToYCoordinate")
    default int mapToYCoordinate(String coordinates) {
        return LocationUtils.parseCoordinate(coordinates, false);
    }
}
