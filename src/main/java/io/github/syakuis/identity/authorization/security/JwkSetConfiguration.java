package io.github.syakuis.oauth2.authorizationserver.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
class JwkSetConfiguration {
    private final RSAPrivateKey signingPrivateKey;
    private final RSAPublicKey validationPublicKey;

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder(validationPublicKey)
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256);
        return new JWKSet(builder.build());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final RsaSigner signer = new RsaSigner(signingPrivateKey);

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            private final JsonParser objectMapper = JsonParserFactory.create();

            @Override
            protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

                String content;
                try {
                    content = this.objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
                } catch (Exception ex) {
                    throw new IllegalStateException("Cannot convert access token to JSON", ex);
                }
                return JwtHelper.encode(content, signer).getEncoded();
            }
        };
        converter.setSigner(signer);
        converter.setVerifier(new RsaVerifier(validationPublicKey));
        return converter;
    }
}
