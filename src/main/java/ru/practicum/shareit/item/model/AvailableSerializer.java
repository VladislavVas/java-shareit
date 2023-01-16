package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AvailableSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String available, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (available.equals("true")) {
            jsonGenerator.writeBoolean(true);
        } else {
            jsonGenerator.writeBoolean(false);
        }
    }
}
