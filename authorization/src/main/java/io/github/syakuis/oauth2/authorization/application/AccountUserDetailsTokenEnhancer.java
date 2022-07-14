package io.github.syakuis.oauth2.authorization.application;

import io.github.syakuis.oauth2.account.model.AccountUserDetails;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-15
 */
@Service
public class AccountUserDetailsTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (authentication.getPrincipal() instanceof AccountUserDetails accountUserDetails) {
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
