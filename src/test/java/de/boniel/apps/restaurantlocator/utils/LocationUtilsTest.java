package de.boniel.apps.restaurantlocator.utils;

import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.fault.ErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocationUtilsTest {

    @Test
    void shouldParseValidXCoordinate() {
        int x = LocationUtils.parseCoordinate("x=10,y=20", true);
        assertEquals(10, x);
    }

    @Test
    void shouldParseValidYCoordinate() {
        int y = LocationUtils.parseCoordinate("x=10,y=20", false);
        assertEquals(20, y);
    }

    @Test
    void shouldThrowForNullCoordinate() {
        ApiException ex = assertThrows(ApiException.class, () ->
                LocationUtils.parseCoordinate(null, true)
        );
        assertEquals(ErrorType.INVALID_COORDINATE, ex.getErrorType());
    }

    @Test
    void shouldThrowForEmptyCoordinate() {
        ApiException ex = assertThrows(ApiException.class, () ->
                LocationUtils.parseCoordinate("", true)
        );
        assertEquals(ErrorType.INVALID_COORDINATE, ex.getErrorType());
    }

    @Test
    void shouldThrowForInvalidFormat() {
        ApiException ex = assertThrows(ApiException.class, () ->
                LocationUtils.parseCoordinate("10,20", true)
        );
        assertEquals(ErrorType.INVALID_COORDINATE, ex.getErrorType());
    }

    @Test
    void shouldThrowForNegativeCoordinate() {
        ApiException ex = assertThrows(ApiException.class, () ->
                LocationUtils.parseCoordinate("x=-5,y=10", true)
        );
        assertEquals(ErrorType.INVALID_COORDINATE, ex.getErrorType());
    }

    @Test
    void shouldReturnCoordinateString() {
        String result = LocationUtils.toCoordinateString(5, 7);
        assertEquals("x=5,y=7", result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 0, 0.0",
            "1, 1, 4, 1, 3.0",
            "2, 5, 2, 1, 4.0",
            "0, 0, 3, 4, 5.0",
            "1, 2, 4, 6, 5.0",
            "5, 5, 8, 9, 5.0"
    })
    void calculateDistance_shouldReturnExpectedResult(
            int fromX, int fromY, int toX, int toY, double expected) {

        double distance = LocationUtils.calculateDistance(fromX, fromY, toX, toY);

        assertEquals(expected, distance, 0.0001,
                () -> String.format("Distance from (%d,%d) to (%d,%d) should be %.2f",
                        fromX, fromY, toX, toY, expected));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 0, 0, true",    // same point, zero radius (edge case)
            "0, 0, 1, 0, 0, true",    // same point, positive radius
            "0, 0, 5, 3, 4, true",    // distance = 5, radius = 5 -> boundary case
            "1, 1, 1, 3, 2, false",   // Example #1 in PDF
            "2, 2, 2, 3, 2, true",    // Example #2 in PDF
            "5, 5, 1, 3, 2, false",   // Example #3 in PDF
            "2, 3, 5, 3, 2, true",    // Example #4 in PDF
            "1, 1, 2, 2, 2, true",
            "1, 1, 1, 2, 2, false",
            "2, 3, 5, 3, 2, true",
            "5, 5, 1, 3, 2, false",
            "4, 4, 3, 7, 7, false",
            "1, 1, 1, 3, 2, false"
    })
    void isWithinRadius_shouldReturnExpectedResult(
            int fromX, int fromY, int radius, int toX, int toY, boolean expected) {

        boolean result = LocationUtils.isWithinRadius(fromX, fromY, radius, toX, toY);

        assertEquals(expected, result,
                () -> String.format("isWithinRadius(%d,%d,radius=%d, %d,%d) should be %s",
                        fromX, fromY, radius, toX, toY, expected));
    }
}
