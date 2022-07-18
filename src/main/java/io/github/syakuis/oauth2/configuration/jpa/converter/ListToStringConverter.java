package io.github.syakuis.oauth2.configuration.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter(autoApply = true)
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> values) {
        return values != null ? String.join(SPLIT_CHAR, values) : null;
    }

    @Override
    public List<String> convertToEntityAttribute(String value) {
        return value != null ? Arrays.asList(value.split(SPLIT_CHAR)) : Collections.emptyList();
    }
}
