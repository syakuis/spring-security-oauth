package io.github.syakuis.identity.core.jpa.converter;

import java.util.Collections;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter
public class SetToStringConverter implements AttributeConverter<Set<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<String> values) {
        return values != null ? String.join(SPLIT_CHAR, values) : null;
    }

    @Override
    public Set<String> convertToEntityAttribute(String value) {
        return value != null ? Set.of(value.split(SPLIT_CHAR)) : Collections.emptySet();
    }
}
