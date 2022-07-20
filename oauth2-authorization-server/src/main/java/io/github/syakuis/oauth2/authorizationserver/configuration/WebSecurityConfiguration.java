package io.github.syakuis.oauth2.authorizationserver.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.introspection-uri}")
    private String introspectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}")
    private String clientSecret;

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .antMatchers("/api-docs/**")
            .antMatchers("/error")
        ;
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
                authorize -> authorize
                    .requestMatchers(
                        new AntPathRequestMatcher("/oauth2/authorize", HttpMethod.POST.name()),
                        new AntPathRequestMatcher("/oauth2/.well-known/jwks.json", HttpMethod.GET.name())
                    )
                    .permitAll()
                    .anyRequest().authenticated());

        Assert.hasText(introspectionUri, "인증 토큰 검증을 위한 인증 서버 설정이 없습니다. (introspectionUri)");
        Assert.hasText(clientId, "인증 토큰 검증을 위한 clientId 설정이 없습니다. (clientId)");
        Assert.hasText(clientSecret, "인증 토큰 검증을 위한 clientSecret 설정이 없습니다. (clientSecret)");

        http.oauth2ResourceServer(oauth2ResourceServer ->
            oauth2ResourceServer.opaqueToken(token -> token.introspectionUri(introspectionUri)
                .introspectionClientCredentials(clientId, clientSecret)));
    }
}