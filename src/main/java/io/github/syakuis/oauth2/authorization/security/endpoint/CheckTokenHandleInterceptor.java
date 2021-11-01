package io.github.syakuis.oauth2.authorization.security.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
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
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private final TokenStore tokenStore;
    private final RedisTemplate redisTemplate;

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    public CheckTokenHandleInterceptor(RedisTemplate redisTemplate, TokenStore tokenStore) {
        this.redisTemplate = redisTemplate;
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

        final ValueOperations<byte[], byte[]> valueOpt = redisTemplate.opsForValue();

        byte[] key = serializationStrategy.serialize(AUTH_TO_ACCESS + token);
        byte[] value = valueOpt.get(key);
        OAuth2AccessToken accessToken = serializationStrategy.deserialize(value, OAuth2AccessToken.class);
        log.debug("====> {}", accessToken);
        request.setAttribute("token", accessToken.getValue());
        return true;
    }
}
