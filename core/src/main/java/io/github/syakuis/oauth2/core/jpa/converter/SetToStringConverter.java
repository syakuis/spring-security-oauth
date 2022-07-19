package io.github.syakuis.oauth2.core.jpa.converter;

import java.util.Collections;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.util.StringUtils;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter
public class SetToStringConverter implements AttributeConverter<Set<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<String> values) {
        return values == null || values.isEmpty() ? null : String.join(SPLIT_CHAR, values);
    }

    @Override
    public Set<String> convertToEntityAttribute(String value) {
        return StringUtils.hasText(value) ? Set.of(value.split(SPLIT_CHAR)) : Collections.emptySet();
    }
}
