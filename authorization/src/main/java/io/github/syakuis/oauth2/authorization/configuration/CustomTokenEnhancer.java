package io.github.syakuis.oauth2.authorization.configuration;

import java.util.HashMap;
import java.util.Map;
import kr.co.parkingcloud.platform.ipom.security.account.application.model.AccountUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-15
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (authentication.getPrincipal() instanceof AccountUser) {
            AccountUser accountUserDetails = (AccountUser) authentication.getPrincipal();

            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("name", accountUserDetails.getName());
            additionalInfo.put("uid", accountUserDetails.getUid());
            additionalInfo.put("additionalInformation", accountUserDetails.getAdditionalInformation());

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        }
        return accessToken;
    }
}
