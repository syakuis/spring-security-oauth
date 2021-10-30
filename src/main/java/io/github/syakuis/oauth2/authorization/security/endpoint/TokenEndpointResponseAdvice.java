package io.github.syakuis.oauth2.authorization.security.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-29
 */
@RestControllerAdvice
public class TokenEndpointResponseAdvice implements ResponseBodyAdvice<OAuth2AccessToken> {
    private final TokenStore tokenStore;

    @Autowired
    public TokenEndpointResponseAdvice(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getContainingClass() == TokenEndpoint.class;
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            OAuth2Authentication authentication = tokenStore.readAuthentication(body.getValue());

            DefaultAuthenticationKeyGenerator defaultAuthenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
            String accessToken = defaultAuthenticationKeyGenerator.extractKey(authentication);
            DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
            defaultOAuth2AccessToken.setAdditionalInformation(body.getAdditionalInformation());
            defaultOAuth2AccessToken.setExpiration(body.getExpiration());
            defaultOAuth2AccessToken.setTokenType(body.getTokenType());

            return defaultOAuth2AccessToken;
    }
}
