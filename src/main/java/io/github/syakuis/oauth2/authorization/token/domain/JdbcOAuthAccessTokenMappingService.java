package io.github.syakuis.oauth2.authorization.token.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
@RequiredArgsConstructor
@Service
@Transactional
public class JdbcOAuthAccessTokenMappingService implements OAuthAccessTokenMappingService {
    private final OAuthAccessTokenMappingRepository oAuthAccessTokenMappingRepository;

    @Override
    public void save(String authorizationId, OAuth2AccessToken accessToken) {
        oAuthAccessTokenMappingRepository.save(OAuthAccessTokenMappingEntity.builder()
            .authorizationId(authorizationId)
            .accessToken(accessToken.getValue())
            .expired(accessToken.getExpiresIn())
            .build());
    }

    @Override
    public String findByAuthorizationId(String authorizationId) {
        OAuthAccessTokenMappingEntity entity = oAuthAccessTokenMappingRepository.findByAuthorizationId(authorizationId).orElseThrow();
        return entity.getAccessToken();
    }
}
