package io.github.syakuis.identity.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-14
 */
@Component
@EnableConfigurationProperties
@PropertySource("classpath:properties/test.properties")
@ConfigurationProperties(prefix = "app.test")
@Data
public class TestProperties {
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
}
