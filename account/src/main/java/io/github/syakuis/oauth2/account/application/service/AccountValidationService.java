package io.github.syakuis.oauth2.account.application.service;

import io.github.syakuis.oauth2.account.application.enums.AccountResultStatus;
import io.github.syakuis.oauth2.account.application.exception.AccountResultStatusException;
import io.github.syakuis.oauth2.account.domain.AccountRepositoryCustom;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-13
 */
@RequiredArgsConstructor
@Service
@Transactional
class AccountValidationService {
    private final AccountRepositoryCustom accountRepositoryCustom;

    public void usernameExists(String username) {
        if (accountRepositoryCustom.existsByUsername(username)) {
            throw new AccountResultStatusException(AccountResultStatus.EXISTS_USERNAME);
        }
    }
}
