package io.github.syakuis.identity.clientregistration.application;

import io.github.syakuis.identity.clientregistration.domain.ClientRegistration;
import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationRepository;
import io.github.syakuis.identity.clientregistration.exception.ClientIdNotFoundException;
import io.github.syakuis.identity.clientregistration.mapper.ClientRegistrationMapper;
import io.github.syakuis.identity.clientregistration.support.ClientKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@RequiredArgsConstructor
@Service
@Transactional
class ClientRegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public ClientRegistration object(String clientId) {
        return ClientRegistrationMapper.INSTANCE.toDto(
            clientRegistrationRepository.findByClientId(clientId).orElseThrow(ClientIdNotFoundException::new));
    }

    public ClientRegistration register(ClientRegistrationRequestBody.Register register) {
        String clientId = ClientKeyGenerator.clientId();
        String clientSecret = passwordEncoder.encode(ClientKeyGenerator.clientSecret());

        return ClientRegistrationMapper.INSTANCE.toDto(
            clientRegistrationRepository.save(
                ClientRegistrationMapper.INSTANCE.register(clientId, clientSecret, register)));
    }
}
