package de.boniel.apps.restaurantlocator.fault;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiException extends RuntimeException {

    private final ErrorType errorType;

    public ApiException() {
        this(ErrorType.INTERNAL_ERROR, "An unexpected error occurred");
    }

    public ApiException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
}
