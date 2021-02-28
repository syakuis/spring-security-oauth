package io.github.syakuis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.config.support.SimpleModelMapper;
import io.github.syakuis.config.support.SimpleObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicBeanConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return SimpleObjectMapper.of(new ObjectMapper());
    }

    @Bean
    public ModelMapper modelMapper() {
        return SimpleModelMapper.of();
    }
}
