package io.github.syakuis.oauth2.authorization.application;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-14
 */

@Slf4j
@FrameworkEndpoint
public class AccessTokenRestController {
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
}
