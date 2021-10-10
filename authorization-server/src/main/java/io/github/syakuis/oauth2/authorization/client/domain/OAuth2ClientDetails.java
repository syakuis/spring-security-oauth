package io.github.syakuis.oauth2.authorization.client.domain;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
public interface OAuth2ClientDetails {
    Long getId();

    String getClientId();

    String getClientSecret();

    java.util.List<String> getResourceIds();

    java.util.List<String> getScopes();

    java.util.List<String> getAuthorizedGrantTypes();

    java.util.Set<String> getWebServerRedirectUri();

    java.util.List<org.springframework.security.core.GrantedAuthority> getAuthorities();

    Integer getAccessTokenValidity();

    Integer getRefreshTokenValidity();

    String getAdditionalInformation();

    java.util.Set<String> getAutoApprove();

    java.time.LocalDateTime getRegisteredOn();

    java.time.LocalDateTime getUpdatedOn();
}
