package io.github.syakuis.oauth2.clientregistration.application;

import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationEntity;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Service
@Transactional
public class ClientRegistrationClientDetailsService implements ClientDetailsService {

    // todo cache
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public ClientDetails loadClientByClientId(
        String clientId) throws ClientRegistrationException {
        ClientRegistrationEntity clientRegistrationEntity = clientRegistrationRepository.findByClientId(clientId)
            .orElseThrow();

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(clientRegistrationEntity.getClientId());
        baseClientDetails.setClientSecret(clientRegistrationEntity.getClientSecret());
        baseClientDetails.setAccessTokenValiditySeconds(clientRegistrationEntity.getAccessTokenValidity());
        baseClientDetails.setRefreshTokenValiditySeconds(clientRegistrationEntity.getRefreshTokenValidity());

        if (clientRegistrationEntity.getAuthorities() != null) {
            baseClientDetails.setAuthorities(clientRegistrationEntity.getAuthorities());
        }
        if (clientRegistrationEntity.getAuthorizedGrantTypes() != null) {
            baseClientDetails.setAuthorizedGrantTypes(clientRegistrationEntity.getAuthorizedGrantTypes());
        }
        if (clientRegistrationEntity.getAdditionalInformation() != null) {
            baseClientDetails.setAdditionalInformation(null);
        }
        if (clientRegistrationEntity.getWebServerRedirectUri() != null) {
            baseClientDetails.setRegisteredRedirectUri(clientRegistrationEntity.getWebServerRedirectUri());
        }
        if (clientRegistrationEntity.getAutoApprove() != null) {
            baseClientDetails.setAutoApproveScopes(clientRegistrationEntity.getAutoApprove());
        }
        if (clientRegistrationEntity.getResourceIds() != null) {
            baseClientDetails.setResourceIds(clientRegistrationEntity.getResourceIds());
        }
        if (clientRegistrationEntity.getScopes() != null) {
            baseClientDetails.setScope(clientRegistrationEntity.getScopes());
        }
        return baseClientDetails;
    }
}
