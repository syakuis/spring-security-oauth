package io.github.syakuis.oauth2.authorization.client.domain;

import io.github.syakuis.oauth2.authorization.client.mapper.OAuth2ClientDetailsEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final OAuth2ClientDetailsRepository oAuth2ClientDetailsRepository;

    public OAuth2ClientDetails save(OAuth2ClientDetailsEntity oAuth2ClientDetailsEntity) {
        return oAuth2ClientDetailsRepository.save(OAuth2ClientDetailsEntityMapper.INSTANCE.updatePassword(passwordEncoder.encode(oAuth2ClientDetailsEntity.getClientSecret()), oAuth2ClientDetailsEntity));
    }
}
