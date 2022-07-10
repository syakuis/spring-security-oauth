package io.github.syakuis.identity.clientregistration.application;

import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationService;
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
class ClientRegistrationRestService {
    private final PasswordEncoder passwordEncoder;
    private final ClientRegistrationService oAuth2ClientDetailsServer;

    public ClientRegistrationResponseDTO.Body register(ClientRegistrationRequestDTO.Register register) {
        String clientId = ClientKeyGenerator.clientId();
        // 최초 등록시 clientSecret 키를 제공하며 보관은 단방향 암호화하여 저장되므로 다시 찾을 수 없다.
        String clientSecret = ClientKeyGenerator.clientSecret();
        return ClientRegistrationResponseDTO.Body
            .create(clientSecret, oAuth2ClientDetailsServer.save(register.toOAuthClientDetailsEntity(clientId, passwordEncoder.encode(clientSecret))));
    }

}
