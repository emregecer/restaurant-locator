package de.boniel.apps.restaurantlocator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCorrectNearbyLocationsFromDataLoader() throws Exception {
        mockMvc.perform(get("/v1/locations/search")
                        .param("x", "5")
                        .param("y", "7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user-location").value("x=5,y=7"))
                .andExpect(jsonPath("$.locations").isArray())
                .andExpect(jsonPath("$.locations[0].name").value("Noodle Nest"))
                .andExpect(jsonPath("$.locations[0].coordinates").value("x=5,y=6"))
                .andExpect(jsonPath("$.locations[0].distance").value("1"));
    }

    @Test
    void shouldFindLocationById() throws Exception {
        mockMvc.perform(get("/v1/locations/{id}", "21e1545c-8b65-4d83-82f9-7fcad4a23114"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Deseado Steakhaus"))
                .andExpect(jsonPath("$.coordinates").value("x=2,y=2"));
    }

    @Test
    void shouldUpsertNewLocationAndRetrieveIt() throws Exception {
        UUID newId = UUID.randomUUID();

        String newLocationJson = """
        {
            "id": "%s",
            "name": "Integration Test New Restaurant",
            "type": "Restaurant",
            "openingHours": "08:00-18:00",
            "image": "http://newimage",
            "coordinates": "x=15,y=25",
            "radius": 4
        }
        """.formatted(newId);

        mockMvc.perform(put("/v1/locations/{id}", newId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newLocationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test New Restaurant"));

        mockMvc.perform(get("/v1/locations/{id}", newId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coordinates").value("x=15,y=25"));
    }
}
