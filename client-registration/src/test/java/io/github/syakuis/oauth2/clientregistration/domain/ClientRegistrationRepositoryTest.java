package io.github.syakuis.oauth2.clientregistration.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.syakuis.oauth2.clientregistration.support.ClientKeyGenerator;
import io.github.syakuis.oauth2.core.AuthorizedGrantType;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-12
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class ClientRegistrationRepositoryTest {
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;


    private final String clientId = ClientKeyGenerator.clientId();

    @Test
    void save() {
        clientRegistrationRepository.save(
            ClientRegistrationEntity.builder()
                .clientId(clientId)
                .clientSecret(ClientKeyGenerator.clientSecret())
                .applicationName("테스트")
                .scope(Set.of("read"))
                .authorizedGrantType(Set.of(AuthorizedGrantType.password))
                .accessTokenValidity(0)
                .refreshTokenValidity(0)
                .registeredBy("test")
                .webServerRedirectUri(Set.of("http://localhost"))
                .build());

        ClientRegistrationEntity clientRegistration = clientRegistrationRepository.findByClientId(clientId).orElseThrow();

        assertNotNull(clientRegistration);
        assertEquals(clientRegistration.getClientId(), clientId);
        assertNotNull(clientRegistration.getClientSecret());
        assertNotNull(clientRegistration.getRegisteredOn());
    }
}