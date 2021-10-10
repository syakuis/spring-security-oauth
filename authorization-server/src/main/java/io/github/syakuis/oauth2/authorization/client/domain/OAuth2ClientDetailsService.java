package io.github.syakuis.oauth2.authorization.client.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@RequiredArgsConstructor
@Service
@Transactional
public class OAuth2ClientDetailsService {
    private final OAuth2ClientDetailsRepository oAuth2ClientDetailsRepository;

    public OAuth2ClientDetails save(OAuth2ClientDetailsEntity oAuth2ClientDetailsEntity) {
        return oAuth2ClientDetailsRepository.save(oAuth2ClientDetailsEntity);
    }

    public OAuth2ClientDetails findByClientId(String clientId) {
        return oAuth2ClientDetailsRepository.findByClientId(clientId).orElseThrow();
    }
}
