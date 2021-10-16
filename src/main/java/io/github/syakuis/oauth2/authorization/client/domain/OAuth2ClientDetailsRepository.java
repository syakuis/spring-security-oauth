package io.github.syakuis.oauth2.authorization.client.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Repository
interface OAuth2ClientDetailsRepository extends JpaRepository<OAuth2ClientDetailsEntity, Long> {
    Optional<OAuth2ClientDetailsEntity> findByClientId(String clientId);
}
