package de.boniel.apps.restaurantlocator.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.format.DoubleSerializer;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class LocationSearchResultDto {

    private UUID id;

    private String name;

    private Coordinates coordinates;

    @JsonSerialize(using = DoubleSerializer.class)
    private double distance;

}
