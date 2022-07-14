package io.github.syakuis.oauth2.account.application;

import io.github.syakuis.oauth2.account.domain.AccountRepository;
import io.github.syakuis.oauth2.account.model.AccountUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AccountUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public AccountUserDetails loadUserByUsername(
        String username) throws UsernameNotFoundException {
        return AccountUserDetails.of(accountRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + " 를 찾을 수 없습니다.")));
    }
}
