package io.github.syakuis.oauth2.authorization.token.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
public interface OAuthAccessTokenMappingRepository extends JpaRepository<OAuthAccessTokenMappingEntity, Long> {
    Optional<OAuthAccessTokenMappingEntity> findByAuthorizationId(String authorizationId);
}
