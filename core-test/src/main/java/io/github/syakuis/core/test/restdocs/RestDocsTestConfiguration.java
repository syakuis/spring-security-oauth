package io.github.syakuis.core.test.restdocs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-14
 */
@TestConfiguration
public class RestDocsTestConfiguration {
    @Value("${server.port}")
    private int port;

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer
            .uris()
            // todo host 표시
            .withScheme("http")
            .withHost("localhost")
            .withPort(8081)
            .and()
            .operationPreprocessors()
            .withRequestDefaults(Preprocessors.prettyPrint())
            .withResponseDefaults(Preprocessors.prettyPrint());
    }
}
