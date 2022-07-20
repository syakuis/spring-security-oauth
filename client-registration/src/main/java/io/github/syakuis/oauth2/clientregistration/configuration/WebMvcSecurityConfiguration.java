package io.github.syakuis.oauth2.clientregistration.configuration;

import io.github.syakuis.oauth2.resourceserver.configuration.EnableResourceServer;
import io.github.syakuis.oauth2.resourceserver.configuration.ResourceServerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@EnableResourceServer
@EnableWebSecurity
public class WebMvcSecurityConfiguration extends WebSecurityConfigurerAdapter implements ResourceServerConfigurer {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(
                authorize -> authorize
                    .anyRequest().authenticated());
    }
}