package io.github.syakuis.oauth2.clientregistration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@EnableWebSecurity
public class WebMvcSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeRequests(
                authorize -> authorize
                    .anyRequest().authenticated()).build();
    }
}