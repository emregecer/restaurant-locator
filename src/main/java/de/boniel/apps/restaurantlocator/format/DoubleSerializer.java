package de.boniel.apps.restaurantlocator.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        BigDecimal rounded = BigDecimal.valueOf(value)
                .setScale(5, RoundingMode.HALF_UP)
                .stripTrailingZeros();

        gen.writeNumber(rounded);
    }

}
