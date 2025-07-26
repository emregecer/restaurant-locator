package de.boniel.apps.restaurantlocator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.boniel.apps.restaurantlocator.model.Coordinates;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Data Transfer Object for Location information.
 * As an improvement, it can be separated as "LocationRequestDto" and "LocationResponseDto" if necessary.
 * For simplicity, this class is used for both request and responses.
 */
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class LocationDto {

    private UUID id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String type;

    @Schema(
            description = "Coordinates in the format x=<non-negative>,y=<non-negative>",
            example = "x=2,y=3"
    )
    @Valid
    @NotNull
    private Coordinates coordinates;

    @Schema(
            description = "Should be a positive integer representing the radius in meters.",
            example = "x=2,y=3"
    )
    @NotNull
    @Positive
    private Integer radius;

    @JsonProperty("opening-hours")
    private String openingHours;

    private String image;

}
