package io.github.syakuis.oauth2.authorization.oauth2.application.client;

import io.github.syakuis.oauth2.authorization.oauth2.domain.client.OAuthClientDetailsEntity;
import io.github.syakuis.oauth2.authorization.oauth2.domain.client.OAuthClientDetailsRepository;
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
    private final OAuthClientDetailsRepository oAuthClientDetailsRepository;

    @Override
    public ClientDetails loadClientByClientId(
        String clientId) throws ClientRegistrationException {
        OAuthClientDetailsEntity oAuthClientDetailsEntity = oAuthClientDetailsRepository.findByClientId(clientId)
            .orElseThrow();

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(oAuthClientDetailsEntity.getClientId());
        baseClientDetails.setClientSecret(oAuthClientDetailsEntity.getClientSecret().replace("{noop}", ""));
        baseClientDetails.setAccessTokenValiditySeconds(oAuthClientDetailsEntity.getAccessTokenValidity());
        baseClientDetails.setRefreshTokenValiditySeconds(oAuthClientDetailsEntity.getRefreshTokenValidity());

        if (oAuthClientDetailsEntity.getAuthorities() != null) {
            baseClientDetails.setAuthorities(oAuthClientDetailsEntity.getAuthorities());
        }
        if (oAuthClientDetailsEntity.getAuthorizedGrantTypes() != null) {
            baseClientDetails.setAuthorizedGrantTypes(oAuthClientDetailsEntity.getAuthorizedGrantTypes());
        }
        if (oAuthClientDetailsEntity.getAdditionalInformation() != null) {
            baseClientDetails.setAdditionalInformation(null);
        }
        if (oAuthClientDetailsEntity.getWebServerRedirectUri() != null) {
            baseClientDetails.setRegisteredRedirectUri(oAuthClientDetailsEntity.getWebServerRedirectUri());
        }
        if (oAuthClientDetailsEntity.getAutoApprove() != null) {
            baseClientDetails.setAutoApproveScopes(oAuthClientDetailsEntity.getAutoApprove());
        }
        if (oAuthClientDetailsEntity.getResourceIds() != null) {
            baseClientDetails.setResourceIds(oAuthClientDetailsEntity.getResourceIds());
        }
        if (oAuthClientDetailsEntity.getScopes() != null) {
            baseClientDetails.setScope(oAuthClientDetailsEntity.getScopes());
        }
        return baseClientDetails;
    }
}
