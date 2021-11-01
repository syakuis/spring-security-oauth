package io.github.syakuis.oauth2.authorization.token.domain;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
public interface OAuthAccessTokenMappingService {
    void save(String authorizationId, OAuth2AccessToken accessToken);

    String findByAuthorizationId(String authorizationId);
}
