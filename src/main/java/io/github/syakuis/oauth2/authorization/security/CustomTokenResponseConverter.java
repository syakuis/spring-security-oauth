package io.github.syakuis.oauth2.authorization.security;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-25
 */
@Slf4j
public class CustomTokenResponseConverter implements
    Converter<Map<String, String>, OAuth2AccessTokenResponse> {

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
        log.debug("====> {}", tokenResponseParameters);

        return OAuth2AccessTokenResponse.withToken(tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN))
            .tokenType(OAuth2AccessToken.TokenType.BEARER)
            .expiresIn(Long.parseLong(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN)))
            .build();
    }
}
