package io.github.syakuis.oauth2.clientregistration.domain;

import io.github.syakuis.oauth2.core.AuthorizedGrantType;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
public interface ClientRegistration {
    Long getId();

    String getClientId();

    String getClientSecret();

    String getApplicationName();

    java.util.Set<String> getResourceId();

    java.util.Set<String> getScope();

    java.util.Set<AuthorizedGrantType> getAuthorizedGrantType();

    java.util.Set<String> getWebServerRedirectUri();

    java.util.Set<GrantedAuthority> getAuthority();

    Integer getAccessTokenValidity();

    Integer getRefreshTokenValidity();

    String getAdditionalInformation();

    java.time.LocalDateTime getRegisteredOn();

    String getRegisteredBy();

    java.time.LocalDateTime getUpdatedOn();
}
