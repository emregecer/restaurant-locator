package de.boniel.apps.restaurantlocator.dto;

import de.boniel.apps.restaurantlocator.fault.ApiException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoordinatesTest {

    @Test
    void toString_ShouldReturnFormattedString() {
        assertThat(new Coordinates(2, 3).toString()).isEqualTo("x=2,y=3");
    }

    @Test
    void fromString_ShouldParseValidString() {
        Coordinates coordinates = Coordinates.fromString("x=2,y=3");

        assertThat(coordinates.getX()).isEqualTo(2);
        assertThat(coordinates.getY()).isEqualTo(3);
    }

    @Test
    void fromString_ShouldParseValidStringWithSpaces() {
        Coordinates coordinates = Coordinates.fromString("x= 10 , y= 20");

        assertThat(coordinates.getX()).isEqualTo(10);
        assertThat(coordinates.getY()).isEqualTo(20);
    }

    @Test
    void fromString_ShouldThrowException_WhenInvalidFormat() {
        assertThatThrownBy(() -> Coordinates.fromString("invalid"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Invalid coordinate format");
    }
}
