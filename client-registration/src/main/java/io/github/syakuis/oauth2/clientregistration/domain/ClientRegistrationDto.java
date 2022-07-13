package io.github.syakuis.oauth2.clientregistration.domain;

import java.time.LocalDateTime;
import java.util.List;
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
    List<String> resourceIds;
    List<String> scopes;
    List<String> authorizedGrantTypes;
    Set<String> webServerRedirectUri;
    List<GrantedAuthority> authorities;
    Integer accessTokenValidity;
    Integer refreshTokenValidity;
    String additionalInformation;
    Set<String> autoApprove;
    LocalDateTime registeredOn;
    String registeredBy;
    LocalDateTime updatedOn;
}
