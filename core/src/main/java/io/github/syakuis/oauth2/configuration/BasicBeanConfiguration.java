package io.github.syakuis.oauth2.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.oauth2.configuration.support.SimpleObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BasicBeanConfiguration {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return SimpleObjectMapper.of(new ObjectMapper());
    }
}

