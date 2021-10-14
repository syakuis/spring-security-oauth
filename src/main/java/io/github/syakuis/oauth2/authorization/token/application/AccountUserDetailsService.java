package io.github.syakuis.oauth2.authorization.token.application;

import io.github.syakuis.oauth2.account.domain.AccountService;
import io.github.syakuis.oauth2.authorization.token.model.OAuth2UserDetails;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@Service
@Transactional
public class AccountUserDetailsService implements UserDetailsService {
    private final AccountService accountService;

    public AccountUserDetailsService(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.notNull(username, "사용자 계정은 필수 입력 값 입니다.");
        try {
            return OAuth2UserDetails.of(accountService.findByUsername(username));
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
