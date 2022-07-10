package io.github.syakuis.identity.client.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-25
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DefaultClientRegistrationRepository implements ClientRegistrationRepository {
    private final io.github.syakuis.identity.clientregistration.domain.ClientRegistrationRepository repository;

    @Override
    public ClientRegistration findByRegistrationId(
        String registrationId) {
        ClientRegistrationEntity clientRegistrationEntity = repository.findByClientId(registrationId).orElseThrow();
        return ClientRegistration.withRegistrationId(clientRegistrationEntity.getClientId()).build();
    }
}
