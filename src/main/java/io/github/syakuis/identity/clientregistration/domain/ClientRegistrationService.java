package io.github.syakuis.identity.clientregistration.domain;

import io.github.syakuis.identity.clientregistration.mapper.ClientRegistrationEntityMapper;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ClientRegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final ClientRegistrationRepository oAuth2ClientDetailsRepository;

    public ClientRegistration save(ClientRegistrationEntity oAuth2ClientDetailsEntity) {
        return oAuth2ClientDetailsRepository.save(ClientRegistrationEntityMapper.INSTANCE.updatePassword(passwordEncoder.encode(oAuth2ClientDetailsEntity.getClientSecret()), oAuth2ClientDetailsEntity));
    }
}
