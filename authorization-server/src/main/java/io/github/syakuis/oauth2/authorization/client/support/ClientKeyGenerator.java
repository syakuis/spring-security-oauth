package io.github.syakuis.oauth2.authorization.client.support;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientKeyGenerator {
    public static String clientId() {
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        pbkdf2PasswordEncoder.setAlgorithm(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA1);
        return pbkdf2PasswordEncoder.encode(UUID.randomUUID().toString());
    }

    public static String clientSecret() {
        char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?")
            .toCharArray();
        return "{noop}" + Base64.getEncoder().encodeToString(RandomStringUtils
            .random(15, 0, possibleCharacters.length - 1, false, false, possibleCharacters, new SecureRandom())
            .getBytes(
                StandardCharsets.UTF_8));
    }
}
