package io.github.syakuis.oauth2.core.jpa.converter;

import io.github.syakuis.oauth2.core.AuthorizedGrantType;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.util.StringUtils;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter
public class AuthorizedGrantTypeToStringConverter implements AttributeConverter<Set<AuthorizedGrantType>, String> {

    @Override
    public String convertToDatabaseColumn(Set<AuthorizedGrantType> values) {
        return values == null || values.isEmpty() ? null : values.stream().map(AuthorizedGrantType::name).collect(Collectors.joining(","));
    }

    @Override
    public Set<AuthorizedGrantType> convertToEntityAttribute(String value) {
        return StringUtils.hasText(value) ?
            Arrays.stream(StringUtils.tokenizeToStringArray(value, ","))
                .map(AuthorizedGrantType::valueOf).collect(
                    Collectors.toSet()) : Collections.emptySet();
    }
}
