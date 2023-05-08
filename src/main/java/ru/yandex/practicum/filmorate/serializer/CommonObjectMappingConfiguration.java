package ru.yandex.practicum.filmorate.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;

@Configuration
public class CommonObjectMappingConfiguration {
    @Bean
    public SimpleModule myCustomCommonModule() {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(Duration.class, new JsonDeserializer<Duration>() {
            @Override
            public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
                return Duration.ofMinutes(jsonParser.getLongValue());
            }
        });

        module.addSerializer(Duration.class, new JsonSerializer<Duration>() {
                    @Override
                    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        long minutes = duration.toMinutes();
                        jsonGenerator.writeNumber(minutes);
                    }
                }
        );

        return module;
    }
}
