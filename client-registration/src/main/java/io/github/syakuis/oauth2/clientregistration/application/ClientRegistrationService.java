package io.github.syakuis.oauth2.clientregistration.application;

import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistration;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationEntity;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationRepository;
import io.github.syakuis.oauth2.clientregistration.exception.ClientIdNotFoundException;
import io.github.syakuis.oauth2.clientregistration.mapper.ClientRegistrationMapper;
import io.github.syakuis.oauth2.clientregistration.support.ClientKeyGenerator;
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

    public ClientRegistration register(ClientRegistrationRequestBody.Register register, String registeredBy) {
        // todo 유일해야 한다.
        String clientId = ClientKeyGenerator.clientId();
        String clientSecret = ClientKeyGenerator.clientSecret();

        return ClientRegistrationMapper.INSTANCE.toDto(
            clientRegistrationRepository.save(
                ClientRegistrationMapper.INSTANCE.register(clientId, passwordEncoder.encode(clientSecret), registeredBy, register)),
            clientSecret);
    }

    public ClientRegistration update(String clientId, ClientRegistrationRequestBody.Register register) {
        ClientRegistrationEntity entity = clientRegistrationRepository.findByClientId(clientId)
            .orElseThrow(ClientIdNotFoundException::new);
        entity.update(ClientRegistrationMapper.INSTANCE.update(register));

        return ClientRegistrationMapper.INSTANCE.toDto(entity);
    }

    public void remove(String clientId) {
        clientRegistrationRepository.delete(
            clientRegistrationRepository.findByClientId(clientId).orElseThrow(ClientIdNotFoundException::new));
    }

    public ClientRegistration refreshingClientSecret(String clientId) {
        ClientRegistrationEntity entity = clientRegistrationRepository.findByClientId(clientId)
            .orElseThrow(ClientIdNotFoundException::new);

        String clientSecret = ClientKeyGenerator.clientSecret();
        entity.refreshingClientSecret(passwordEncoder.encode(clientSecret));

        return ClientRegistrationMapper.INSTANCE.toDto(entity, clientSecret);
    }
}
