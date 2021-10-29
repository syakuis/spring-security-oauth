package io.github.syakuis.oauth2.authorization.token.model;

import io.github.syakuis.oauth2.account.domain.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OAuth2UserDetails implements UserDetails {
    private static final long serialVersionUID = -4957604136920453114L;
    String password;
    String username;
    Set<GrantedAuthority> authorities;
    boolean accountNonExpired;
    boolean accountNonLocked;
    boolean credentialsNonExpired;
    boolean enabled;
    String name;
    UUID uid;
    Map<String, String> additionalInformation;

    public static OAuth2UserDetails of(Account account) {
        return OAuth2UserDetails.builder()
            .username(account.getUsername())
            .name(account.getName())
            .password(account.getPassword())
            .uid(account.getUid())
            .accountNonLocked(!account.getBlocked())
            .enabled(!account.getDisabled())
            .authorities(Collections.emptySet())
            .accountNonExpired(true)
            .credentialsNonExpired(true)
            .build();
    }
}
