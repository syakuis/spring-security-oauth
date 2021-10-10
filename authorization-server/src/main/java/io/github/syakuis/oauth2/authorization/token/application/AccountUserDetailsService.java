package io.github.syakuis.oauth2.authorization.token.application;

import io.github.syakuis.oauth2.authorization.account.domain.AccountService;
import io.github.syakuis.oauth2.authorization.token.model.OAuth2UserDetails;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        try {
            return OAuth2UserDetails.of(accountService.findByUsername(username));
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
