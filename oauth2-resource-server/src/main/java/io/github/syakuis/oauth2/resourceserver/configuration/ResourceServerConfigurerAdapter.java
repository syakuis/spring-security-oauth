package io.github.syakuis.oauth2.resourceserver.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-20
 */
public class ResourceServerConfigurerAdapter implements ResourceServerConfigurer {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }

}

