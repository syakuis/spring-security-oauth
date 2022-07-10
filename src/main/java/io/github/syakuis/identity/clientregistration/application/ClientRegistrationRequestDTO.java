package io.github.syakuis.identity.clientregistration.application;

import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationEntity;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ClientRegistrationRequestDTO {

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    static class Register {
        List<String> resourceIds;
        List<String> scopes;
        List<String> authorizedGrantTypes;
        Set<String> webServerRedirectUri;
        List<GrantedAuthority> authorities;
        Integer accessTokenValidity;
        Integer refreshTokenValidity;
        String additionalInformation;
        Set<String> autoApprove;

        ClientRegistrationEntity toOAuthClientDetailsEntity(String clientId, String clientSecret) {
            return ClientRegistrationEntity.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scopes(this.scopes)
                .accessTokenValidity(this.accessTokenValidity)
                .refreshTokenValidity(this.refreshTokenValidity)
                .authorities(this.authorities)
                .additionalInformation(this.additionalInformation)
                .autoApprove(this.autoApprove)
                .authorizedGrantTypes(this.authorizedGrantTypes)
                .resourceIds(this.resourceIds)
                .webServerRedirectUri(this.webServerRedirectUri)
                .build();
        }
    }
}
