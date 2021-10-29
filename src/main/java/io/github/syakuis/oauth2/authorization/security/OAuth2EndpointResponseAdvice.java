package io.github.syakuis.oauth2.authorization.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-29
 */
@Slf4j
@RestControllerAdvice
public class OAuth2EndpointResponseAdvice implements ResponseBodyAdvice<OAuth2AccessToken> {
    private final TokenStore tokenStore;

    @Autowired
    public OAuth2EndpointResponseAdvice(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(body.getValue());
        log.debug("{}", authentication);

        DefaultAuthenticationKeyGenerator defaultAuthenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
        String accessToken = defaultAuthenticationKeyGenerator.extractKey(authentication);
        log.debug("{}", accessToken);
        return new DefaultOAuth2AccessToken(accessToken);
    }
}
