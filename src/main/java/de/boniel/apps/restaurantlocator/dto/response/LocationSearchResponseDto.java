package de.boniel.apps.restaurantlocator.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.boniel.apps.restaurantlocator.dto.Coordinates;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LocationSearchResponseDto {

    @JsonProperty("user-location")
    private Coordinates userLocation;

    private List<LocationSearchResultDto> locations;

}
