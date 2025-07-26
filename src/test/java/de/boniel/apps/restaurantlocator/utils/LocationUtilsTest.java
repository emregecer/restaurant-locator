package de.boniel.apps.restaurantlocator.utils;

import de.boniel.apps.restaurantlocator.fault.ApiException;
import de.boniel.apps.restaurantlocator.fault.ErrorType;
import org.junit.jupiter.api.Test;

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
}
