package io.github.syakuis.identity.account.application;

import io.github.syakuis.identity.account.application.enums.AccountResultStatus;
import io.github.syakuis.identity.account.application.exception.AccountResultStatusException;
import io.github.syakuis.identity.account.application.model.AccountCommand;
import io.github.syakuis.identity.account.application.service.SignupAccountService;
import io.github.syakuis.identity.account.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-09
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/accounts/signup")
final class SignupRestController {
    private final SignupAccountService signupAccountService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> signup(@RequestBody AccountCommand.Signup signup) {
        if (signupAccountService.account(signup.getUsername()).isPresent()) {
            throw new AccountResultStatusException(AccountResultStatus.REGISTERED_ACCOUNT);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(signupAccountService.signup(signup));
    }
}
