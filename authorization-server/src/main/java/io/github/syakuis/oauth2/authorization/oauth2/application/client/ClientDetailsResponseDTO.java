package io.github.syakuis.oauth2.authorization.oauth2.application.client;

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
public class ClientDetailsResponseDTO {

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

        static Body create(io.github.syakuis.oauth2.authorization.oauth2.domain.client.OAuthClientDetailsEntity oAuthClientDetailsEntity) {
            return Body.builder()
                .clientId(oAuthClientDetailsEntity.getClientId())
                .clientSecret(oAuthClientDetailsEntity.getClientSecret())
                .accessTokenValidity(oAuthClientDetailsEntity.getAccessTokenValidity())
                .build();
        }
    }
}
