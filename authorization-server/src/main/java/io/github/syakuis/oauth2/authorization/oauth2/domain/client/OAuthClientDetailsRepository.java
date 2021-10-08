package io.github.syakuis.oauth2.authorization.oauth2.domain.client;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Repository
public interface OAuthClientDetailsRepository extends JpaRepository<OAuthClientDetailsEntity, Long> {
    Optional<OAuthClientDetailsEntity> findByClientId(String clientId);
}
