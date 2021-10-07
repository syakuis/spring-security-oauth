package io.github.syakuis.oauth2.authorization.configuration.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleModelMapper {
    public static ModelMapper of() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
            .setSkipNullEnabled(true);
        return modelMapper;
    }
}
