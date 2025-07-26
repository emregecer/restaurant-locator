package de.boniel.apps.restaurantlocator.mapper;

import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

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

}
