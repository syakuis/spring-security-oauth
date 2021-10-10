package io.github.syakuis.oauth2.authorization.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.DefaultWebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        return new DefaultWebSecurityExpressionHandler();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(expressionHandler());
        List<AccessDecisionVoter<?>> decisionVoters = Collections.singletonList(webExpressionVoter);
        return new AffirmativeBased(decisionVoters);
    }


    // https://docs.spring.io/spring-security/site/docs/5.1.5.RELEASE/reference/htmlsingle/#filter-security-interceptor
    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        requestMap.put(new AntPathRequestMatcher("/api/main"), SecurityConfig.createList("hasAnyRole('ROLE_TEST')"));
        return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, expressionHandler());
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource());

        filterSecurityInterceptor.afterPropertiesSet();
        return filterSecurityInterceptor;
    }

    @Bean
    public WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator() {
        return new DefaultWebInvocationPrivilegeEvaluator(filterSecurityInterceptor());
    }

    @Bean
    public UserDetailsService userDetailsService() {

        // TODO REST API 변경 혹은 View 테이블 제공 받기
        UserDetails user = User.builder()
            .username("test").password("")
            .roles("TEST")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * PasswordEncoderFactories.createDelegatingPasswordEncoder();
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(EncodeId.BCRYPT.value(), new BCryptPasswordEncoder());
        encoders.put(EncodeId.PBKDF2.value(), new Pbkdf2PasswordEncoder());
        encoders.put(EncodeId.SCRYPT.value(), new SCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(EncodeId.BCRYPT.value(), encoders);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin().disable()
            .authorizeRequests(
                authorize -> authorize.anyRequest().not()
                    .authenticated())
            .addFilterAt(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
            .exceptionHandling();
    }
}
