package de.boniel.apps.restaurantlocator.utils;

import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.fault.ErrorType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationUtils {

    private static final Pattern COORDINATE_PATTERN =
            Pattern.compile("^x\\s*=\\s*\\d+\\s*,\\s*y\\s*=\\s*\\d+$"); // Allows spaces around '=' and ','

    public static int parseCoordinate(String coordinateStr, boolean isX) {
        if (coordinateStr == null || !COORDINATE_PATTERN.matcher(coordinateStr).matches()) {
            throw new ApiException(
                    ErrorType.INVALID_COORDINATE,
                    "Invalid coordinate format: " + coordinateStr + ". Expected format: x=<non-negative>,y=<non-negative>"
            );
        }

        String[] parts = coordinateStr.split(",");

        String xPart = parts[0].replaceAll("\\s+", "").substring(2);
        String yPart = parts[1].replaceAll("\\s+", "").substring(2);

        int value = Integer.parseInt(isX ? xPart : yPart);

        if (value < 0) {
            throw new ApiException(ErrorType.INVALID_COORDINATE, "Coordinate must be non-negative, got: " + value);
        }

        return value;
    }
}
