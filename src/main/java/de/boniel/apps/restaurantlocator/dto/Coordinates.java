package de.boniel.apps.restaurantlocator.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static de.boniel.apps.restaurantlocator.utils.LocationUtils.parseCoordinate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coordinates {

    private int x;
    private int y;

    @JsonValue
    @Override
    public String toString() {
        return String.format("x=%d,y=%d", x, y);
    }

    @JsonCreator
    public static Coordinates fromString(String value) {
        return new Coordinates(
                parseCoordinate(value, true),
                parseCoordinate(value, false)
        );
    }

}
