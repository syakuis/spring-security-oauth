package io.github.syakuis.oauth2.authorization.token.service;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
public interface AccessTokenService {
    String getAccessToken(String authorizationId);
}
