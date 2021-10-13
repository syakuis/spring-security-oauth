package io.github.syakuis.oauth2.configuration.jpa.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter
public class JsonToStringConverter implements AttributeConverter<String, String>, Serializable {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String json) {
        try {
            return json != null ? objectMapper.writeValueAsString(json) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String value) {
        return value;
    }
}
