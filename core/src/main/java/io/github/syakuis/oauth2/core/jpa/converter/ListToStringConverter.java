package io.github.syakuis.oauth2.core.jpa.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.util.StringUtils;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter(autoApply = true)
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> values) {
        return values == null || values.isEmpty() ? null : String.join(SPLIT_CHAR, values);
    }

    @Override
    public List<String> convertToEntityAttribute(String value) {
        return StringUtils.hasText(value) ? Arrays.asList(value.split(SPLIT_CHAR)) : Collections.emptyList();
    }
}
