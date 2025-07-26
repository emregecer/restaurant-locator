package de.boniel.apps.restaurantlocator.fault;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorBody {

    private int errorCode;
    private String errorMessage;
    private LocalDateTime timestamp;

}
