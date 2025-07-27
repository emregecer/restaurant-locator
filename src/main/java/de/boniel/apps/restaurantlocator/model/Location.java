package de.boniel.apps.restaurantlocator.model;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
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

    /**
     * Helper method to calculate the squared Euclidean distance from this location to the given coordinates.
     *
     * @param coordinates Coordinates to which the distance is calculated.
     * @return The squared distance from this location to the given coordinates.
     */
    public int calculateDistanceSquared(Coordinates coordinates) {
        int dx = coordinates.getX() - xCoordinate;
        int dy = coordinates.getY() - yCoordinate;
        return dx * dx + dy * dy;
    }

    /**
     * Calculates the Euclidean distance from this location to the given coordinates.
     * @param coordinates Coordinates to which the distance is calculated.
     * @return The distance from this location to the given coordinates.
     */
    public double calculateDistance(Coordinates coordinates) {
        return Math.sqrt(calculateDistanceSquared(coordinates));
    }

    /**
     * Helper method to calculate the squared radius of this location
     * @return The squared radius of this location.
     */
    public int calculateRadiusSquared() {
        return radius * radius;
    }
}
