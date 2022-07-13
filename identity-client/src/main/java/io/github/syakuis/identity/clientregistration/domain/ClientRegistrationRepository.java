package io.github.syakuis.identity.clientregistration.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public interface ClientRegistrationRepository extends Repository<ClientRegistrationEntity, Long> {
    Optional<ClientRegistrationEntity> findByClientId(String clientId);
    ClientRegistrationEntity save(ClientRegistrationEntity clientRegistrationEntity);
    void delete(ClientRegistrationEntity clientRegistrationEntity);
}
