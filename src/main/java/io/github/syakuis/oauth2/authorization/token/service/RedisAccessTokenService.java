package io.github.syakuis.oauth2.authorization.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisAccessTokenService implements AccessTokenService {
    private final StringRedisTemplate redisTemplate;
    private static final String AUTH_TO_ACCESS = "auth_to_access:";

    @Override
    public String getAccessToken(String authorizationId) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(AUTH_TO_ACCESS + authorizationId);
    }
}
