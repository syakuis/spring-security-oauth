package io.github.syakuis.oauth2.authorization.oauth2.application.client;

import io.github.syakuis.oauth2.authorization.oauth2.domain.client.OAuthClientDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@RequiredArgsConstructor
@Service
@Transactional
class OAuth2ClientDetailsService {

    private final OAuthClientDetailsRepository oAuthClientDetailsRepository;

    public ClientDetailsResponseDTO.Body register(ClientDetailsRequestDTO.Register register) {
        return ClientDetailsResponseDTO.Body
            .create(oAuthClientDetailsRepository.save(register.toOAuthClientDetails()));
    }

}
