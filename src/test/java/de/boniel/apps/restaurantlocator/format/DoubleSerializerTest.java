package de.boniel.apps.restaurantlocator.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DoubleSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Double.class, new DoubleSerializer());
        module.addSerializer(double.class, new DoubleSerializer());

        objectMapper.registerModule(module);
    }

    @Test
    void shouldSerializeDoubleWithFiveDecimals() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(1.4142135623730951);

        assertThat(json).isEqualTo("1.41421");
    }

    @Test
    void shouldRoundCorrectlyToFiveDecimals() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(3.141592653589793);

        assertThat(json).isEqualTo("3.14159");
    }

    @Test
    void shouldRemoveTrailingZerosForFiveDecimals() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(1.4000);

        assertThat(json).isEqualTo("1.4");
    }
}
