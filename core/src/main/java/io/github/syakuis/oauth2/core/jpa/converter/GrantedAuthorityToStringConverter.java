package io.github.syakuis.oauth2.core.jpa.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter
public class GrantedAuthorityToStringConverter implements AttributeConverter<Set<GrantedAuthority>, String> {

    @Override
    public String convertToDatabaseColumn(Set<GrantedAuthority> values) {
        return values != null ? values.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","))
            : null;
    }

    @Override
    public Set<GrantedAuthority> convertToEntityAttribute(String value) {
        return value != null ?
            Arrays.stream(StringUtils.tokenizeToStringArray(value, ","))
                .map(SimpleGrantedAuthority::new).collect(
                    Collectors.toSet()) : Collections.emptySet();
    }
}
