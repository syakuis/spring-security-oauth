package io.github.syakuis.oauth2.account.model;

import io.github.syakuis.oauth2.account.domain.AccountEntity;
import java.io.Serial;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-15
 */
@Builder
@Getter
@ToString
public class AccountUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = -4957604136920453114L;
    @NotNull
    private final String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final String name;
    private final UUID uid;
    private final Map<String, String> additionalInformation;

    public static AccountUserDetails of(AccountEntity accountEntity) {
        return AccountUserDetails.builder()
            .username(accountEntity.getUsername())
            .name(accountEntity.getName())
            .password(accountEntity.getPassword())
            .uid(accountEntity.getUid())
            .accountNonLocked(!accountEntity.isBlocked())
            .enabled(!accountEntity.isDisabled())
            // todo role 부여 필요
            .authorities(Collections.emptySet())
            .accountNonExpired(true)
            .credentialsNonExpired(true)
            .build();
    }
}
