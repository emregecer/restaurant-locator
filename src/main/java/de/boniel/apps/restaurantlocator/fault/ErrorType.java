package de.boniel.apps.restaurantlocator.fault;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorType {

    INTERNAL_ERROR(1, HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(2, HttpStatus.BAD_REQUEST),

    INVALID_COORDINATE(50, HttpStatus.BAD_REQUEST),
    LOCATION_NOT_FOUND(70, HttpStatus.NOT_FOUND);

    private final int code;
    private final HttpStatus httpStatus;

}
