package de.boniel.apps.restaurantlocator.model;

import de.boniel.apps.restaurantlocator.fault.ApiException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoordinatesTest {

    @Test
    void toString_ShouldReturnFormattedString() {
        Coordinates coords = new Coordinates(2, 3);
        assertThat(coords.toString()).isEqualTo("x=2,y=3");
    }

    @Test
    void fromString_ShouldParseValidString() {
        Coordinates coords = Coordinates.fromString("x=2,y=3");

        assertThat(coords.getX()).isEqualTo(2);
        assertThat(coords.getY()).isEqualTo(3);
    }

    @Test
    void fromString_ShouldParseValidStringWithSpaces() {
        Coordinates coords = Coordinates.fromString("x= 10 , y= 20");

        assertThat(coords.getX()).isEqualTo(10);
        assertThat(coords.getY()).isEqualTo(20);
    }

    @Test
    void fromString_ShouldThrowException_WhenInvalidFormat() {
        assertThatThrownBy(() -> Coordinates.fromString("invalid"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Invalid coordinate format");
    }
}
