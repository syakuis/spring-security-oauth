package io.github.syakuis.oauth2.account.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v1/accounts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeRequests(
                authorize -> authorize.anyRequest().authenticated())
        ;
    }
}