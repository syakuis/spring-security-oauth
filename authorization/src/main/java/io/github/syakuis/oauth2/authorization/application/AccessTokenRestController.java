package io.github.syakuis.oauth2.authorization.application;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-14
 */

@FrameworkEndpoint
public class AccessTokenRestController {
//    @Resource(name = "tokenServices")
//    private AuthorizationServerTokenServices tokenServices;
//
//    @DeleteMapping("/oauth/token")
//    @ResponseStatus(HttpStatus.OK)
//    public void revoke(HttpServletRequest request) {
//        BearerTokenExtractor bearerTokenExtractor = new BearerTokenExtractor();
//        Authentication authentication = bearerTokenExtractor.extract(request);
//        if (authentication != null && authentication.getPrincipal() != null) {
//            ((ConsumerTokenServices) tokenServices).revokeToken(authentication.getPrincipal().toString());
//        }
//    }
}
