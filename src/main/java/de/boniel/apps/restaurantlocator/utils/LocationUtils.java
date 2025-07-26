package de.boniel.apps.restaurantlocator.utils;

import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.fault.ErrorType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationUtils {

    private static final Pattern COORDINATE_PATTERN = Pattern.compile("^x=\\d+,y=\\d+$");

    public static int parseCoordinate(String coordinateStr, boolean isX) {
        if (coordinateStr == null || !COORDINATE_PATTERN.matcher(coordinateStr).matches()) {
            throw new ApiException(
                    ErrorType.INVALID_COORDINATE,
                    "Invalid coordinate format: " + coordinateStr + ". Expected x=<non-negative>,y=<non-negative>"
            );
        }

        String[] parts = coordinateStr.split(",");
        int value = Integer.parseInt(isX ? parts[0].substring(2) : parts[1].substring(2));

        if (value < 0) {
            throw new ApiException(
                    ErrorType.INVALID_COORDINATE,
                    "Coordinate must be non-negative, got: " + value
            );
        }

        return value;
    }

    public static String toCoordinateString(int x, int y) {
        return "x=" + x + ",y=" + y;
    }
}
