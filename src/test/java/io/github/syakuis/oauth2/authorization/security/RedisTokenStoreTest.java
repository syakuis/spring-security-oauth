package io.github.syakuis.oauth2.authorization.security;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
@Slf4j
@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisTokenStoreTest {
    @Autowired
    private RedisTemplate<String, OAuth2AccessToken> redisTemplate;

    @Test
    void test() {
        String AUTH_TO_ACCESS = "auth_to_access:";
        String accessToken = "4731f06f5a08e76e47b34517127d8009";
        ValueOperations<String, OAuth2AccessToken> valueOperations = redisTemplate.opsForValue();


        Set<String> keys = redisTemplate.keys(AUTH_TO_ACCESS + "*");
        log.debug("{}", keys);

        OAuth2AccessToken value = valueOperations.get(AUTH_TO_ACCESS + accessToken);
        log.debug("{}", value);
    }
}