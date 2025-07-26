package de.boniel.apps.restaurantlocator.dto.response;

import de.boniel.apps.restaurantlocator.model.Coordinates;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
public class LocationSearchResultDto {

    private UUID id;
    private String name;
    private Coordinates coordinates;
    private double distance;

}
