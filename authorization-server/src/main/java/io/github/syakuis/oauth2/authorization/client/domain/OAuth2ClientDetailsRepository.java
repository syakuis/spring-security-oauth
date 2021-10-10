package io.github.syakuis.oauth2.authorization.client.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Repository
interface OAuth2ClientDetailsRepository extends JpaRepository<OAuth2ClientDetailsEntity, Long> {
    Optional<OAuth2ClientDetailsEntity> findByClientId(String clientId);
}
