package io.github.syakuis.todo.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // spring jwt properties 설정으로 빈 생성됨.
    private final JwtDecoder jwtDecoder;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
    private String introspectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @Value("${app.security.oauth2.jwt.resourceserver.opaque-access-token-disabled:false}")
    private boolean opaqueAccessTokenDisabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeRequests(
                authorize -> authorize
                    .anyRequest().authenticated())
            .exceptionHandling()
            .and()
            .oauth2ResourceServer(oauth2ResourceServer -> {
                // Test를 위해 사용.
                if (opaqueAccessTokenDisabled) {
                    oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder));
                } else {
                    oauth2ResourceServer.opaqueToken(token -> {
                        //                        token.introspectionUri(introspectionUri).introspectionClientCredentials(clientId, clientSecret));
                        token.introspector(
                            new AuthoritiesOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret));
                    });
                }
            });
    }
}
