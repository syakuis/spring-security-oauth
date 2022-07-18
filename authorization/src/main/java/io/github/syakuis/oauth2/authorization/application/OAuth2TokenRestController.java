package io.github.syakuis.oauth2.authorization.application;

import com.nimbusds.jose.jwk.JWKSet;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-14
 */

@Slf4j
@RequiredArgsConstructor
@FrameworkEndpoint
public class OAuth2TokenRestController {
    private final JWKSet jwkSet;
    @Resource(name = "tokenServices")
    private AuthorizationServerTokenServices tokenServices;

    @DeleteMapping("/oauth2/token")
    public ResponseEntity<Void> revoke(HttpServletRequest request) {
        BearerTokenExtractor bearerTokenExtractor = new BearerTokenExtractor();
        Authentication authentication = bearerTokenExtractor.extract(request);
        if (authentication != null && authentication.getPrincipal() != null) {
            boolean success = ((ConsumerTokenServices) tokenServices).revokeToken(authentication.getPrincipal().toString());
            if (success) {
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * JSON 웹 서명(JWS)을 얻기 위한 공개 키를 제공한다.
     * JWS 는 데이터의 무결성 , 즉 JSON 웹 토큰(JWT) 의 데이터를 확인하기 위한 다양한 암호화 메커니즘을 설명 하는 IETF에서 만든 사양입니다
     */
    @GetMapping(path = "/oauth2/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> keys() {
        return this.jwkSet.toJSONObject();
    }
}
