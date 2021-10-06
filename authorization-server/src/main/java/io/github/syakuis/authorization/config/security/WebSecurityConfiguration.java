package io.github.syakuis.authorization.config.security;

import io.github.syakuis.authorization.component.authorization.service.RoleService;
import io.github.syakuis.authorization.component.member.service.MemberUserDetailsService;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
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

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final MemberUserDetailsService memberUserDetailsService;
    private final RoleService roleService;

    @Value("${spring.h2.console.enabled}")
    private boolean h2ConsoleEnabled;

    private RoleHierarchy roleHierarchy() {
        String roles = roleService.getHierarchy();
        log.debug("RoleHierarchy : {}", roles);

        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(roles);
        return roleHierarchy;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(expressionHandler());
        List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(
            new RoleHierarchyVoter(roleHierarchy()),
            new RoleVoter(),
            new AuthenticatedVoter(),
            webExpressionVoter);
        return new AffirmativeBased(decisionVoters);
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return webSecurityExpressionHandler;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        requestMap.put(new AntPathRequestMatcher("/api/**"), SecurityConfig.createList("hasRole('ROLE_USER')"));
        return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, expressionHandler());
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource());

        filterSecurityInterceptor.afterPropertiesSet();
        return filterSecurityInterceptor;
    }

    @Bean
    public WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator() throws Exception {
        return new DefaultWebInvocationPrivilegeEvaluator(filterSecurityInterceptor());
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

    /**
     * protected void configure(AuthenticationManagerBuilder auth) throws Exception { auth.inMemoryAuthentication()
     * .withUser("test").password(passwordEncoder().encode("1234")) .roles("USER") .and()
     * .withUser("admin").password(passwordEncoder().encode("!@#$")) .roles("ADMIN", "USER");
     *
     * }
     *
     * @return UserDetailsService
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return memberUserDetailsService;
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
        if (h2ConsoleEnabled) {
            web.ignoring()
                .antMatchers("/h2-console/**");
        }
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