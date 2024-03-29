package io.github.syakuis.oauth2.authorizationserver.configuration;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @Autowired
    public AuthorizationServerConfiguration(
        AuthenticationManager authenticationManager,
        @Qualifier("accountUserDetailsService") UserDetailsService userDetailsService,
        @Qualifier("clientRegistrationClientDetailsService") ClientDetailsService clientDetailsService,
        TokenStore tokenStore,
        @Qualifier("accountUserDetailsTokenEnhancer") TokenEnhancer tokenEnhancer,
        @Qualifier("jwtAccessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
        this.tokenStore = tokenStore;
        this.tokenEnhancer = tokenEnhancer;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
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
            .tokenStore(tokenStore)
            .reuseRefreshTokens(false)
            .tokenEnhancer(tokenEnhancerChain)
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
            .pathMapping("/oauth/check_token", "/oauth2/token-check")
            .pathMapping("/oauth/token", "/oauth2/token")
            .pathMapping("/oauth/authorize", "/oauth2/authorize")
            .pathMapping("/oauth/confirm_access", "/oauth2/confirm-access")
            .pathMapping("/oauth/token_key", "/oauth2/token-key")
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
        ;
    }

    // todo error response handler
    /*@Bean
    public WebResponseExceptionTranslator loggingExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<ErrorResponsePayload> translate(Exception e) throws Exception {
                // This is the line that prints the stack trace to the log. You can customise this to format the trace etc if you like
                e.printStackTrace();

                // Carry on handling the exception
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                OAuth2Exception excBody = responseEntity.getBody();

                return new ResponseEntity<>(ErrorResponsePayload.builder()
                    .build(), headers, responseEntity.getStatusCode());
            }
        };
    }*/
}
