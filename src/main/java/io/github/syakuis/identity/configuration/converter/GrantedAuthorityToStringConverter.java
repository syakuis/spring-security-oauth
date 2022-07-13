package io.github.syakuis.oauth2.configuration.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Converter
public class GrantedAuthorityToStringConverter implements AttributeConverter<List<GrantedAuthority>, String> {
    @Override
    public String convertToDatabaseColumn(List<GrantedAuthority> values) {
        return values != null ? values.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining()) : null;
    }

    @Override
    public List<GrantedAuthority> convertToEntityAttribute(String value) {
        return value != null ? AuthorityUtils.commaSeparatedStringToAuthorityList(value) : Collections.emptyList();
    }
}
