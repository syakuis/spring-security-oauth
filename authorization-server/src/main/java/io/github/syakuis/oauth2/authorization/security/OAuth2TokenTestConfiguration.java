package io.github.syakuis.oauth2.authorization.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@Configuration
@Profile("test")
public class OAuth2TokenTestConfiguration {
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
