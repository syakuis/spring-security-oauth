package io.github.syakuis.oauth2.account.application;

import io.github.syakuis.oauth2.account.application.model.AccountCommand.Signup;
import io.github.syakuis.oauth2.account.application.service.SignupAccountService;
import io.github.syakuis.oauth2.account.domain.Account;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-09
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts/signup")
final class SignupRestController {
    private final SignupAccountService signupAccountService;

    @PostMapping
    public ResponseEntity<Account> signup(@RequestBody @Valid Signup signup) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signupAccountService.signup(signup));
    }

    @GetMapping(value = "/duplicate-username", produces = MediaType.TEXT_PLAIN_VALUE)
    public String duplicateUsername(@RequestParam("username") String username) {
        return String.valueOf(signupAccountService.usernameExists(username));
    }
}
