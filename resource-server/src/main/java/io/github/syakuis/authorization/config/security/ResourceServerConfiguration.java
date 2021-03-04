package io.github.syakuis.authorization.config.security;

import java.io.IOException;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * ResourceServer Spring Security Configuration
 *
 * @author Seok Kyun. Choi.
 * @since 2019-04-24
 */
@RequiredArgsConstructor
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private final SecurityExpressionHandler<FilterInvocation> expressionHandler;
    private final FilterSecurityInterceptor filterSecurityInterceptor;
    @Value("${authorization.oauth2.jwt.public-key-location}")
    private String publicKeyLocation;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws IOException {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        converter.setVerifierKey(IOUtils.toString(resolver.getResource(publicKeyLocation).getInputStream(), Charset.defaultCharset()));
        return converter;
    }

    @Bean
    public TokenStore tokenStore() throws IOException {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true).expressionHandler(expressionHandler);

        /*
        curl -u iplms:iplms -X POST "http://localhost:8080/oauth/check_token" -d "token=토큰"
         */
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setClientId("clientId");
        tokenService.setClientSecret("1234");
        tokenService.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        resources.tokenServices(tokenService);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeRequests(
                authorize -> authorize.anyRequest().not()
                    .authenticated())
            .addFilterAt(filterSecurityInterceptor, FilterSecurityInterceptor.class)
            .exceptionHandling();
    }
}
