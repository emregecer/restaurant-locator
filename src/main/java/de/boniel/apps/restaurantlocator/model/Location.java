package de.boniel.apps.restaurantlocator.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
public class Location {

    private UUID id;
    private String name;
    private String type;
    private int xCoordinate;
    private int yCoordinate;
    private int radius;
    private String openingHours;
    private String image;

}
