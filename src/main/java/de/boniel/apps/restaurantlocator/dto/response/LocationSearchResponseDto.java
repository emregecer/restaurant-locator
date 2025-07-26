package de.boniel.apps.restaurantlocator.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LocationSearchResponseDto {

    private String userCoordinates;
    private List<LocationSearchResultDto> locations;
}
