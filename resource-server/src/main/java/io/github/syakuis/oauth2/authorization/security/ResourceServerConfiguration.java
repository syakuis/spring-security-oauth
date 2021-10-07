package io.github.syakuis.oauth2.authorization.security;

import java.io.IOException;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
    private final UserDetailsService userDetailsService;

    @Value("${authorization.oauth2.jwt.public-key-location}")
    private String publicKeyLocation;


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws IOException {
        DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(defaultUserAuthenticationConverter);
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(defaultAccessTokenConverter);
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
        // TODO REST API 변경 혹은 View 테이블 제공 받기
        /*
        curl -u iplms:iplms -X POST "http://localhost:8080/oauth/check_token" -d "token=토큰"
         */
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setClientId("clientId");
        tokenService.setClientSecret("1234");
        tokenService.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        tokenService.setAccessTokenConverter(accessTokenConverter());
        resources.tokenServices(tokenService);
    }
}
