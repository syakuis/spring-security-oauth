package io.github.syakuis.oauth2.authorization.client.application;

import io.github.syakuis.oauth2.authorization.client.domain.OAuth2ClientDetails;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class OAuth2ClientDetailsResponseDTO {

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    static class Body {
        List<String> resourceIds;
        List<String> scopes;
        List<String> authorizedGrantTypes;
        Set<String> webServerRedirectUri;
        List<GrantedAuthority> authorities;
        Integer accessTokenValidity;
        Integer refreshTokenValidity;
        String additionalInformation;
        Set<String> autoApprove;
        String clientId;
        String clientSecret;

        static Body create(String clientSecret, OAuth2ClientDetails oAuthClientDetails) {
            return Body.builder()
                .clientId(oAuthClientDetails.getClientId())
                .clientSecret(clientSecret)
                .accessTokenValidity(oAuthClientDetails.getAccessTokenValidity())
                .build();
        }
    }
}
