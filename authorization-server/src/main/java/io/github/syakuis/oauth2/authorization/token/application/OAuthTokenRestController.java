package io.github.syakuis.oauth2.authorization.token.application;

import com.nimbusds.jose.jwk.JWKSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Seok Kyun. Choi.
 * @since 2019-05-28
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2/v1/token")
class OAuthTokenRestController {
    private final JWKSet jwkSet;

    @Resource(name = "tokenServices")
    private ConsumerTokenServices tokenServices;

    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest request) {
        BearerTokenExtractor bearerTokenExtractor = new BearerTokenExtractor();
        Authentication authentication = bearerTokenExtractor.extract(request);
        if (authentication != null && authentication.getPrincipal() != null){
            tokenServices.revokeToken(authentication.getPrincipal().toString());
        }
    }

    /**
     * JWT 서명을 얻기 위한 공개 키를 제공한다.
     * @return
     */
    @GetMapping(value = "/keys", produces = MediaType.APPLICATION_JSON_VALUE)
    public String keys() {
        return this.jwkSet.toString();
    }
}

