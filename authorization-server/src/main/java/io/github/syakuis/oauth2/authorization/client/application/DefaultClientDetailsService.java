package io.github.syakuis.oauth2.authorization.client.application;

import io.github.syakuis.oauth2.authorization.client.domain.OAuth2ClientDetails;
import io.github.syakuis.oauth2.authorization.client.domain.OAuth2ClientDetailsEntity;
import io.github.syakuis.oauth2.authorization.client.domain.OAuth2ClientDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DefaultClientDetailsService implements ClientDetailsService {

    // TODO cache
    private final OAuth2ClientDetailsService oAuth2ClientDetailsService;

    @Override
    public ClientDetails loadClientByClientId(
        String clientId) throws ClientRegistrationException {
        OAuth2ClientDetails oAuthClientDetails = oAuth2ClientDetailsService.findByClientId(clientId);

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(oAuthClientDetails.getClientId());
        baseClientDetails.setClientSecret(oAuthClientDetails.getClientSecret());
        baseClientDetails.setAccessTokenValiditySeconds(oAuthClientDetails.getAccessTokenValidity());
        baseClientDetails.setRefreshTokenValiditySeconds(oAuthClientDetails.getRefreshTokenValidity());

        if (oAuthClientDetails.getAuthorities() != null) {
            baseClientDetails.setAuthorities(oAuthClientDetails.getAuthorities());
        }
        if (oAuthClientDetails.getAuthorizedGrantTypes() != null) {
            baseClientDetails.setAuthorizedGrantTypes(oAuthClientDetails.getAuthorizedGrantTypes());
        }
        if (oAuthClientDetails.getAdditionalInformation() != null) {
            baseClientDetails.setAdditionalInformation(null);
        }
        if (oAuthClientDetails.getWebServerRedirectUri() != null) {
            baseClientDetails.setRegisteredRedirectUri(oAuthClientDetails.getWebServerRedirectUri());
        }
        if (oAuthClientDetails.getAutoApprove() != null) {
            baseClientDetails.setAutoApproveScopes(oAuthClientDetails.getAutoApprove());
        }
        if (oAuthClientDetails.getResourceIds() != null) {
            baseClientDetails.setResourceIds(oAuthClientDetails.getResourceIds());
        }
        if (oAuthClientDetails.getScopes() != null) {
            baseClientDetails.setScope(oAuthClientDetails.getScopes());
        }
        return baseClientDetails;
    }
}
