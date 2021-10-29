package io.github.syakuis.oauth2.authorization.token.service;

import io.github.syakuis.oauth2.authorization.token.model.OAuth2UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@Slf4j
@Service
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        log.debug("===>OAuth2AccessToken: {}", accessToken);
        log.debug("===>OAuth2RefreshToken: {}", accessToken.getRefreshToken());
        log.debug("===>OAuth2Authentication: {}", authentication);
        if (authentication.getPrincipal() instanceof OAuth2UserDetails) {
            OAuth2UserDetails oAuth2UserDetails = (OAuth2UserDetails) authentication.getPrincipal();

            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("name", oAuth2UserDetails.getName());
            additionalInfo.put("uid", oAuth2UserDetails.getUid());
            additionalInfo.put("additionalInformation", oAuth2UserDetails.getAdditionalInformation());

            DefaultOAuth2AccessToken auth2AccessToken = new DefaultOAuth2AccessToken(accessToken.getValue());
            auth2AccessToken.setRefreshToken(accessToken.getRefreshToken());

            return auth2AccessToken;
        }
        return accessToken;
    }
}

