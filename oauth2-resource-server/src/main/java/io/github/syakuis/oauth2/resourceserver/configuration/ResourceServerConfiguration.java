package io.github.syakuis.oauth2.resourceserver.configuration;

import io.github.syakuis.oauth2.core.security.handler.OAuth2AccessDeniedHandler;
import io.github.syakuis.oauth2.core.security.handler.OAuth2ForbiddenEntryPoint;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Order(3)
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.oauth2.resourceserver.opaque-token.introspection-uri:#{null}}")
    private String introspectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id:#{null}}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret:#{null}}")
    private String clientSecret;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:#{null}}")
    private String jwkSetUri;

    private List<ResourceServerConfigurer> configurers = Collections.emptyList();

    @Autowired(required = false)
    public void setConfigurers(List<ResourceServerConfigurer> configurers) {
        this.configurers = configurers;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .antMatchers("/api-docs/**")
            .antMatchers("/error")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(new OAuth2ForbiddenEntryPoint())
                    .accessDeniedHandler(new OAuth2AccessDeniedHandler())
            )
        ;

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

        for (ResourceServerConfigurer configurer : configurers) {
            configurer.configure(http);
        }
        if (configurers.isEmpty()) {
            http.authorizeRequests().anyRequest().authenticated();
        }
    }
}