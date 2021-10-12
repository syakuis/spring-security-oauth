package io.github.syakuis.account.domain;

import io.github.syakuis.account.mapper.AccountEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public Account save(AccountEntity account) {
        return accountRepository.save(
            AccountEntityMapper.INSTANCE.updatePassword(passwordEncoder.encode(account.getPassword()), account));
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }
}