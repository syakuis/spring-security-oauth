package io.github.syakuis.oauth2.clientregistration.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Repository
interface ClientRegistrationRepository extends JpaRepository<ClientRegistrationEntity, Long> {
    Optional<ClientRegistrationEntity> findByClientId(String clientId);
}
