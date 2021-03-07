package io.github.syakuis.authorization.config.security;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
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

/**
 * ResourceServer Spring Security Configuration
 *
 * @author Seok Kyun. Choi.
 * @since 2019-04-24
 */
@RequiredArgsConstructor
@Profile("test")
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Value("${authorization.oauth2.jwt.public-key-location}")
    private String publicKeyLocation;

    private final UserDetailsService userDetailsService;

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
        requestMap.put(new AntPathRequestMatcher("/api/profile"), SecurityConfig.createList("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')"));
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
    public JwtAccessTokenConverter resourceAccessTokenConverter() throws IOException {
        DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(defaultUserAuthenticationConverter);
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(defaultAccessTokenConverter);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        converter.setVerifierKey(IOUtils.toString(resolver.getResource(publicKeyLocation).getInputStream(), Charset.defaultCharset()));
        return converter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeRequests(
                authorize -> authorize.anyRequest().not()
                    .authenticated())
            .addFilterAt(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
            .exceptionHandling();
    }
}
