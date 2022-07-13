package io.github.syakuis.oauth2.clientregistration.domain;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
public interface ClientRegistration {
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

    String getRegisteredBy();

    java.time.LocalDateTime getUpdatedOn();
}
