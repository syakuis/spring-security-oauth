package io.github.syakuis.oauth2.clientregistration.domain;

import io.github.syakuis.oauth2.core.AuthorizedGrantType;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-08
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ClientRegistrationDto implements ClientRegistration {
    Long id;
    String clientId;
    String clientSecret;
    String applicationName;
    Set<String> resourceId;
    Set<String> scope;
    Set<AuthorizedGrantType> authorizedGrantType;
    Set<String> webServerRedirectUri;
    Set<GrantedAuthority> authority;
    Integer accessTokenValidity;
    Integer refreshTokenValidity;
    String additionalInformation;
    LocalDateTime registeredOn;
    String registeredBy;
    LocalDateTime updatedOn;
}
