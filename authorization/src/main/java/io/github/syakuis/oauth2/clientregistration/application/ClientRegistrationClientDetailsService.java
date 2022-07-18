package io.github.syakuis.oauth2.clientregistration.application;

import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationEntity;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationRepository;
import io.github.syakuis.oauth2.core.AuthorizedGrantType;
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

        if (clientRegistrationEntity.getAuthority() != null) {
            baseClientDetails.setAuthorities(clientRegistrationEntity.getAuthority());
        }
        if (clientRegistrationEntity.getAuthorizedGrantType() != null) {
            baseClientDetails.setAuthorizedGrantTypes(clientRegistrationEntity.getAuthorizedGrantType()
                .stream().map(AuthorizedGrantType::name).toList());
        }
        if (clientRegistrationEntity.getAdditionalInformation() != null) {
            baseClientDetails.setAdditionalInformation(null);
        }
        if (clientRegistrationEntity.getWebServerRedirectUri() != null) {
            baseClientDetails.setRegisteredRedirectUri(clientRegistrationEntity.getWebServerRedirectUri());
        }
        if (clientRegistrationEntity.getResourceId() != null) {
            baseClientDetails.setResourceIds(clientRegistrationEntity.getResourceId());
        }
        if (clientRegistrationEntity.getScope() != null) {
            baseClientDetails.setScope(clientRegistrationEntity.getScope());
        }
        return baseClientDetails;
    }
}
