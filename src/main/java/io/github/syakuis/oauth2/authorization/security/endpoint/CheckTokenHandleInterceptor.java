package io.github.syakuis.oauth2.authorization.security.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
@Slf4j
public class CheckTokenHandleInterceptor implements HandlerInterceptor {
    private final TokenStore tokenStore;

    public CheckTokenHandleInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private boolean isTarget(HttpServletRequest request) {
        return new AntPathRequestMatcher("/oauth/check_token", HttpMethod.POST.name()).matches(request);
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!isTarget(request)) {
            return true;
        }

        String token = request.getParameter("token");
        log.debug("====> {}", token);

        OAuth2Authentication authentication = tokenStore.readAuthentication(token);
        log.debug("====> {}", authentication);
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);
        log.debug("====> {}", accessToken);
        return true;
    }
}
