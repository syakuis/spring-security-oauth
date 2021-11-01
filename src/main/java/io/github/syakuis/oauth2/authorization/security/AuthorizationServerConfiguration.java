package io.github.syakuis.oauth2.authorization.security;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ClientDetailsService clientDetailsService;
    private final TokenStore tokenStore;
    private final TokenEnhancer tokenEnhancer;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final RedisTemplate<String, OAuth2AccessToken> redisTemplate;

    @Autowired
    public AuthorizationServerConfiguration(
        AuthenticationManager authenticationManager,
        @Qualifier("accountUserDetailsService") UserDetailsService userDetailsService,
        @Qualifier("defaultClientDetailsService") ClientDetailsService clientDetailsService,
        TokenStore tokenStore,
        @Qualifier("customTokenEnhancer") TokenEnhancer tokenEnhancer,
        @Qualifier("jwtAccessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter,
        RedisTemplate<String, OAuth2AccessToken> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
        this.tokenStore = tokenStore;
        this.tokenEnhancer = tokenEnhancer;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
        if (!(tokenStore instanceof RedisTokenStore)) {
            throw new IllegalArgumentException("redis token store 만 지원합니다.");
        }
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(tokenEnhancer);
        return defaultTokenServices;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, jwtAccessTokenConverter));

        endpoints
            .pathMapping("/oauth/check_token", "/oauth2/v1/token/check")
            .tokenStore(tokenStore)
            .reuseRefreshTokens(true)
            .tokenEnhancer(tokenEnhancerChain)
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
        ;
    }
}
