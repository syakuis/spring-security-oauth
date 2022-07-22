package io.github.syakuis.oauth2.resourceserver.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-20
 */
public interface ResourceServerConfigurer {
    void configure(HttpSecurity http) throws Exception;
}
