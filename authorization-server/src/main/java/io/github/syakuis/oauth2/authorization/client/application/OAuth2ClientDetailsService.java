package io.github.syakuis.oauth2.authorization.client.application;

import io.github.syakuis.oauth2.authorization.client.domain.OAuth2ClientDetailsRepository;
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

    private final OAuth2ClientDetailsRepository oAuthClientDetailsRepository;

    public OAuth2ClientDetailsResponseDTO.Body register(OAuth2ClientDetailsRequestDTO.Register register) {
        return OAuth2ClientDetailsResponseDTO.Body
            .create(oAuthClientDetailsRepository.save(register.toOAuthClientDetails()));
    }

}
