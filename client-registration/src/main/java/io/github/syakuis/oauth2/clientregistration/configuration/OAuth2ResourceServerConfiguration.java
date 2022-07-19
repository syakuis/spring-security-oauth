package io.github.syakuis.oauth2.clientregistration.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class OAuth2ResourceServerConfiguration {

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
    private String introspectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain oauth2ResourceServerFilterChain(HttpSecurity http) throws Exception {
        if (introspectionUri != null) {
            Assert.hasText(introspectionUri, "인증 토큰 검증을 위한 인증 서버 설정이 없습니다. (introspectionUri)");
            Assert.hasText(clientId, "인증 토큰 검증을 위한 clientId 설정이 없습니다. (clientId)");
            Assert.hasText(clientSecret, "인증 토큰 검증을 위한 clientSecret 설정이 없습니다. (clientSecret)");

            http.oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.opaqueToken(token -> token.introspectionUri(introspectionUri)
                    .introspectionClientCredentials(clientId, clientSecret)));
        } else {
            Assert.hasText(clientSecret, "인증 토큰 검증을 위한 jwkSetUri 설정이 없습니다. (jwkSetUri)");
            http.oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt -> jwt.decoder(NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build())
                    .jwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter())));
        }

        return http.build();
    }
}