package io.github.syakuis.oauth2.authorization.token.application;

import io.github.syakuis.oauth2.authorization.account.domain.AccountService;
import io.github.syakuis.oauth2.authorization.token.model.OAuth2UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@RequiredArgsConstructor
@Service
public class AccountUserDetailsService implements UserDetailsService {
    private final AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return OAuth2UserDetails.of(accountService.findByUsername(username));
    }
}
