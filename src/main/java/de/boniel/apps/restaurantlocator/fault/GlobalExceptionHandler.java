package de.boniel.apps.restaurantlocator.fault;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        log.error("API Exception occured", ex);

        ErrorType errorType = ex.getErrorType();

        Map<String, Object> body = Map.of(
                "errorCode", errorType.getCode(),
                "errorType", errorType.name(),
                "message", ex.getMessage()
        );

        return ResponseEntity.status(errorType.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAnyException(Exception ex) {
        log.error("Exception occured", ex);

        ApiException apiException = new ApiException();

        Map<String, Object> errorResponse = createErrorResponse(apiException);

        return ResponseEntity.status(apiException.getErrorType().getHttpStatus()).body(errorResponse);
    }

    private Map<String, Object> createErrorResponse(ApiException ex) {
        ErrorType errorType = ex.getErrorType();
        return Map.of(
                "errorCode", String.valueOf(errorType.getCode()),
                "errorType", errorType.name(),
                "message", ex.getMessage()
        );
    }

}
