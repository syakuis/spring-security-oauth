package io.github.syakuis.oauth2.clientregistration.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.syakuis.oauth2.clientregistration.support.ClientKeyGenerator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-12
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ClientRegistrationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;


    private final String clientId = ClientKeyGenerator.clientId();

    @Test
    void save() {
        clientRegistrationRepository.save(
            ClientRegistrationEntity.builder()
                .clientId(clientId)
                .clientSecret(ClientKeyGenerator.clientSecret())
                .scopes(List.of("read"))
                .authorizedGrantTypes(List.of("password"))
                .accessTokenValidity(0)
                .refreshTokenValidity(0)
                .build());

        ClientRegistrationEntity clientRegistration = clientRegistrationRepository.findByClientId(clientId).orElseThrow();

        assertNotNull(clientRegistration);
        assertEquals(clientRegistration.getClientId(), clientId);
        assertNotNull(clientRegistration.getClientSecret());
        assertNotNull(clientRegistration.getRegisteredOn());
    }
}