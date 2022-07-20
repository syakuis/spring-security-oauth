package io.github.syakuis.oauth2.authorizationserver.token.application;

import io.github.syakuis.oauth2.account.domain.AccountService;
import io.github.syakuis.oauth2.authorizationserver.token.model.OAuth2UserDetails;
import javax.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@Service
@Transactional(readOnly = true)
public class DefaultUserDetailsService implements UserDetailsService {
    private final AccountService accountService;

    public DefaultUserDetailsService(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "사용자 계정은 필수 입력 값 입니다.");
        try {
            return OAuth2UserDetails.of(accountService.account(username));
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
