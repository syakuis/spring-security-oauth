package io.github.syakuis.oauth2.authorization.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class WebConfiguration {
    @Bean
    @Autowired
    public HttpMessageConverter<Object> createJsonHttpMessageConverter(ObjectMapper objectMapper) {
        ObjectMapper mapper = objectMapper.copy();
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(mapper);
        return jackson2HttpMessageConverter;
    }
}
