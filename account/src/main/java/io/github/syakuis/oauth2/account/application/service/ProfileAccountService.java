package io.github.syakuis.oauth2.account.application.service;

import io.github.syakuis.oauth2.account.application.enums.AccountResultStatus;
import io.github.syakuis.oauth2.account.application.exception.AccountResultStatusException;
import io.github.syakuis.oauth2.account.application.mapper.AccountMapper;
import io.github.syakuis.oauth2.account.application.model.AccountCommand;
import io.github.syakuis.oauth2.account.domain.AccountEntity;
import io.github.syakuis.oauth2.account.domain.AccountRepository;
import io.github.syakuis.oauth2.account.domain.Profile;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-24
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ProfileAccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public Profile account(UUID uid) {
        AccountEntity accountEntity = accountRepository.findByUid(uid).orElseThrow(() -> {
            throw new AccountResultStatusException(AccountResultStatus.ACCOUNT_NOT_FOUND, "uid: " + uid);
        });
        return AccountMapper.INSTANCE.toProfileDto(accountEntity);
    }

    public Profile update(UUID uid, AccountCommand.Profile profile) {

        AccountEntity accountEntity = accountRepository.findByUid(uid)
            .orElseThrow(() -> new AccountResultStatusException(AccountResultStatus.ACCOUNT_NOT_FOUND, "uid: " + uid));

        accountEntity.updatePassword(passwordEncoder.encode(profile.getPassword()));

        AccountEntity source = AccountMapper.INSTANCE.fromProfile(profile);
        accountEntity.updateProfile(source);

        return AccountMapper.INSTANCE.toProfileDto(accountEntity);
    }
}
