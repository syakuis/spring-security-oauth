package io.github.syakuis.oauth2.authorization.security.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 * <p>
 * origin
 * <p>
 * {"access_token":"eyJhbG....7pA","token_type":"bearer","refresh_token":"eyJhbGciO....","expires_in":315359999,"scope":"read","uid":"c7b782b5-f8b5-11eb-972f-02a208506bd6","additionalInformation":null,"name":"test","jti":"78d2940c-6ee6-438b-a8e3-ab4f2176399d"}
 */
@Slf4j
@Component
@Aspect
public class TokenEndpointResponseAop {
    private final TokenStore tokenStore;
    private final DefaultAuthenticationKeyGenerator defaultAuthenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Autowired
    public TokenEndpointResponseAop(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public Object body(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (!(result instanceof ResponseEntity)) {
            return result;
        }

        ResponseEntity<OAuth2AccessToken> response = (ResponseEntity<OAuth2AccessToken>) joinPoint.proceed();
        assert response != null;
        OAuth2AccessToken body = response.getBody();
        assert body != null;
        String accessToken = body.getValue();
        OAuth2RefreshToken oAuth2RefreshToken = body.getRefreshToken();

        OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);

        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(defaultAuthenticationKeyGenerator.extractKey(authentication));
        defaultOAuth2AccessToken.setAdditionalInformation(body.getAdditionalInformation());
        defaultOAuth2AccessToken.setExpiration(body.getExpiration());
        defaultOAuth2AccessToken.setTokenType(body.getTokenType());

        return new ResponseEntity<OAuth2AccessToken>(defaultOAuth2AccessToken, response.getHeaders(), response.getStatusCode());
    }

}
