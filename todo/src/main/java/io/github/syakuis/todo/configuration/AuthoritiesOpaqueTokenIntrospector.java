package io.github.syakuis.todo.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class AuthoritiesOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OpaqueTokenIntrospector delegate;
//    private final UserDetailsService userDetailsService;

    // TODO JWT ROLE 사용할 것.
    public AuthoritiesOpaqueTokenIntrospector(/*UserDetailsService userDetailsService,*/ String introspectionUri,
        String clientId,
        String clientSecret) {
//        this.userDetailsService = userDetailsService;
        this.delegate = new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        return new DefaultOAuth2AuthenticatedPrincipal(
            principal.getName(), principal.getAttributes(), extractAuthorities(principal));
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        Assert.notNull(principal, "principal cannot be null");

        List<GrantedAuthority> authorities = new ArrayList<>();

        getScopes(principal).ifPresent(authorities::addAll);

        getAuthorities(principal).ifPresent(authorities::addAll);

        return authorities;
    }

    private Optional<Collection<? extends GrantedAuthority>> getScopes(OAuth2AuthenticatedPrincipal principal) {
        List<String> scopes = principal.getAttribute(OAuth2IntrospectionClaimNames.SCOPE);

        if (scopes == null) {
            return Optional.empty();
        }

        return Optional.of(scopes.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()));
    }

    private Optional<Collection<? extends GrantedAuthority>> getAuthorities(OAuth2AuthenticatedPrincipal principal) {
        String username = principal.getAttribute("user_name");

        if (!StringUtils.hasText(username)) {
            return Optional.empty();
        }

//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return Optional.of(List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
