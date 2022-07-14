package io.github.syakuis.oauth2.authorization.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-04-28
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {
    private final RedisConnectionFactory redisConnectionFactory;
    @Bean
    RedisTemplate<String, byte[]> redisTemplate() {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
