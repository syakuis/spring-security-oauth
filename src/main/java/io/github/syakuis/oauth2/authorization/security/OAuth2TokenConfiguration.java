package io.github.syakuis.oauth2.authorization.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@RequiredArgsConstructor
@Configuration
public class OAuth2TokenConfiguration {
    private final RedisConnectionFactory redisConnectionFactory;
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
}
