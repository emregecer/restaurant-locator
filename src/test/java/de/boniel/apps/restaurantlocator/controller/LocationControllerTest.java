package de.boniel.apps.restaurantlocator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.boniel.apps.restaurantlocator.dto.Coordinates;
import de.boniel.apps.restaurantlocator.dto.LocationDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResponseDto;
import de.boniel.apps.restaurantlocator.dto.response.LocationSearchResultDto;
import de.boniel.apps.restaurantlocator.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LocationService locationService;

    @Test
    void shouldReturnAllLocations() throws Exception {
        LocationDto dto = LocationDto.builder()
                .id(UUID.randomUUID())
                .name("Fire Tiger")
                .coordinates(new Coordinates(2, 3))
                .openingHours("10:00AM-11:00PM")
                .radius(1)
                .build();

        when(locationService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fire Tiger"))
                .andExpect(jsonPath("$[0].coordinates").value("x=2,y=3"))
                .andExpect(jsonPath("$[0].opening-hours").value("10:00AM-11:00PM"));
    }

    @Test
    void shouldSearchNearbyLocations() throws Exception {
        int x = 1;
        int y = 3;
        Coordinates coordinates = new Coordinates(x, y);

        LocationSearchResultDto loc1 = LocationSearchResultDto.builder()
                .id(UUID.randomUUID())
                .name("Location 1")
                .build();

        LocationSearchResponseDto mockResponse = LocationSearchResponseDto.builder()
                .userLocation(coordinates)
                .locations(List.of(loc1))
                .build();

        when(locationService.searchNearbyLocations(any(Coordinates.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/v1/locations/search")
                        .param("x", String.valueOf(x))
                        .param("y", String.valueOf(y))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user-location").value(coordinates.toString()))
                .andExpect(jsonPath("$.locations").isArray())
                .andExpect(jsonPath("$.locations[0].name").value(loc1.getName()));

        verify(locationService).searchNearbyLocations(argThat(coords ->
                coords.getX() == x && coords.getY() == y
        ));
    }

    @Test
    void shouldReturnLocationById() throws Exception {
        UUID id = UUID.randomUUID();

        LocationDto dto = LocationDto.builder()
                .id(id)
                .name("Sushi Bar")
                .coordinates(new Coordinates(5, 6))
                .openingHours("10:00AM-11:00PM")
                .radius(2)
                .build();

        when(locationService.getLocationById(id)).thenReturn(dto);

        mockMvc.perform(get("/v1/locations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sushi Bar"))
                .andExpect(jsonPath("$.coordinates").value("x=5,y=6"));
    }

    @Test
    void shouldUpsertLocation() throws Exception {
        UUID id = UUID.randomUUID();

        LocationDto locationDto = LocationDto.builder()
                .id(id)
                .name("Green Garden")
                .type("Restaurant")
                .coordinates(new Coordinates(10, 20))
                .radius(3)
                .build();

        when(locationService.upsertLocation(eq(id), any(LocationDto.class)))
                .thenReturn(locationDto);

        mockMvc.perform(put("/v1/locations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Green Garden"))
                .andExpect(jsonPath("$.type").value("Restaurant"))
                .andExpect(jsonPath("$.coordinates").value("x=10,y=20"));
    }

    @Test
    void shouldFailValidation_onUpsertLocation_WhenMissingName() throws Exception {
        UUID id = UUID.randomUUID();
        LocationDto locationDto = LocationDto.builder()
                .id(id)
                .type("Restaurant")
                .coordinates(new Coordinates(10, 20))
                .radius(3)
                .build();

        mockMvc.perform(put("/v1/locations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailValidation_onUpsertLocation_WhenMissingType() throws Exception {
        UUID id = UUID.randomUUID();
        LocationDto locationDto = LocationDto.builder()
                .id(id)
                .name("Green Garden")
                .coordinates(new Coordinates(10, 20))
                .radius(3)
                .build();

        mockMvc.perform(put("/v1/locations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailValidation_onUpsertLocation_WhenCoordinatesInvalid() throws Exception {
        UUID id = UUID.randomUUID();
        LocationDto locationDto = LocationDto.builder()
                .id(id)
                .name("Green Garden")
                .type("Restaurant")
                .coordinates(new Coordinates(-10, -20))
                .radius(3)
                .build();

        mockMvc.perform(put("/v1/locations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailValidation_onUpsertLocationWhenRadiusInvalid() throws Exception {
        UUID id = UUID.randomUUID();
        LocationDto locationDto = LocationDto.builder()
                .id(id)
                .name("Green Garden")
                .type("Restaurant")
                .coordinates(new Coordinates(-10, -20))
                .radius(-3)
                .build();

        mockMvc.perform(put("/v1/locations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isBadRequest());
    }
}