package de.boniel.apps.restaurantlocator.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class LocationSearchResultDto {

    private String id;
    private String name;
    private String coordinates;
    private double distance;

}
