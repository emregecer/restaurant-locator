package de.boniel.apps.restaurantlocator.model;

import de.boniel.apps.restaurantlocator.dto.Coordinates;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class LocationTest {

    @Test
    void shouldCalculateDistanceSquaredCorrectly() {
        Location location = Location.builder()
                .id(UUID.randomUUID())
                .name("TestLoc")
                .xCoordinate(2)
                .yCoordinate(2)
                .radius(3)
                .build();

        Coordinates userCoordinates = new Coordinates(4, 3);

        int distSq = location.calculateDistanceSquared(userCoordinates);

        assertThat(distSq).isEqualTo(5);
    }

    @Test
    void shouldCalculateDistanceCorrectly() {
        Location location = Location.builder()
                .id(UUID.randomUUID())
                .name("TestLoc")
                .xCoordinate(2)
                .yCoordinate(2)
                .radius(3)
                .build();

        Coordinates userCoordinates = new Coordinates(4, 3);

        double distance = location.calculateDistance(userCoordinates);

        assertThat(distance).isCloseTo(Math.sqrt(5), within(0.0001));
    }

    @Test
    void shouldCalculateRadiusSquaredCorrectly() {
        Location location = Location.builder()
                .id(UUID.randomUUID())
                .name("TestLoc")
                .xCoordinate(0)
                .yCoordinate(0)
                .radius(3)
                .build();

        int radiusSq = location.calculateSquared();

        assertThat(radiusSq).isEqualTo(9);
    }

    @Test
    void shouldDetermineIfUserIsInsideRadius() {
        Location location = Location.builder()
                .xCoordinate(2)
                .yCoordinate(2)
                .radius(3)
                .build();

        Coordinates userCoordinates = new Coordinates(4, 3);

        boolean insideRadius = location.calculateDistanceSquared(userCoordinates) <= location.calculateSquared();
        assertThat(insideRadius).isTrue();

        Coordinates farUser = new Coordinates(10, 10);

        boolean outsideRadius = location.calculateDistanceSquared(farUser) <= location.calculateSquared();
        assertThat(outsideRadius).isFalse();
    }
}
