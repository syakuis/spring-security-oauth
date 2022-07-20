package io.github.syakuis.oauth2.authorizationserver.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration extends DelegatingWebMvcConfiguration {
    private final ObjectMapper objectMapper;

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api-docs/**").addResourceLocations("classpath:/static/api-docs/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(stringConverter);
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        super.addDefaultHttpMessageConverters(converters);
    }
}