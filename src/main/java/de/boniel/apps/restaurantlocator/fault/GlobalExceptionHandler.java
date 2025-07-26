package de.boniel.apps.restaurantlocator.fault;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorBody> handleApiException(ApiException ex) {
        log.error("API Exception occurred", ex);

        ErrorBody errorBody = createErrorBody(ex);

        return createErrorResponseEntity(ex.getErrorType().getHttpStatus(), errorBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred", ex);

        ErrorBody errorBody = createErrorBody(ErrorType.VALIDATION_ERROR, "Validation failed");

        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, errorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> handleAnyException(Exception ex) {
        log.error("Exception occurred", ex);
        ApiException apiException = new ApiException();

        ErrorBody errorBody = createErrorBody(apiException);

        return createErrorResponseEntity(apiException.getErrorType().getHttpStatus(), errorBody);
    }

    private ResponseEntity<ErrorBody> createErrorResponseEntity(HttpStatus status, ErrorBody errorBody) {
        return ResponseEntity.status(status).body(errorBody);
    }

    private ErrorBody createErrorBody(ApiException ex) {
        ErrorType errorType = ex.getErrorType();
        return createErrorBody(errorType, ex.getMessage());
    }

    private ErrorBody createErrorBody(ErrorType errorType, String message) {
        return ErrorBody.builder()
                .errorCode(errorType.getCode())
                .errorMessage(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
