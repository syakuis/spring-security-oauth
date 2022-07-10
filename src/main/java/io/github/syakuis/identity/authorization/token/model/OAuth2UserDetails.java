package io.github.syakuis.identity.authorization.token.model;

import io.github.syakuis.identity.account.domain.Account;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
            .uid(account.getUid())
            .accountNonLocked(!account.isBlocked())
            .enabled(!account.isDisabled())
            .authorities(Collections.emptySet())
            .accountNonExpired(true)
            .credentialsNonExpired(true)
            .build();
    }
}
