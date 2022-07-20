package io.github.syakuis.oauth2.account.application;

import io.github.syakuis.oauth2.account.application.enums.AccountResultStatus;
import io.github.syakuis.oauth2.account.application.exception.AccountResultStatusException;
import io.github.syakuis.oauth2.account.application.model.AccountCommand;
import io.github.syakuis.oauth2.account.application.service.PasswordAccountService;
import io.github.syakuis.oauth2.account.application.service.ProfileAccountService;
import io.github.syakuis.oauth2.account.domain.Profile;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/v1/accounts")
class ProfileAccountRestController {
    private final ProfileAccountService profileAccountService;
    private final PasswordAccountService passwordAccountService;

    // todo jwt 방식으로 BearerTokenAuthentication 클래스로 정보를 받을 수 있다. 인증 정보를 받기 위해 자체 개발하여 라이브러리를 제공하자.
    private UUID getUid(Authentication authentication) {
        Assert.notNull(authentication, "the class not be null");
        return UUID.fromString((String) authentication.getName());
    }

    @GetMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Profile profile(Authentication authentication) {
        UUID uid = getUid(authentication);
        return profileAccountService.account(uid);
    }

    @PatchMapping(path = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Profile update(Authentication authentication, @Valid @RequestBody AccountCommand.Profile profile) {
        UUID uid = getUid(authentication);
        return profileAccountService.update(uid, profile);
    }

    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(Authentication authentication, @Valid @RequestBody AccountCommand.ChangePassword password) {
        UUID uid = getUid(authentication);

        if (!password.matches() || !passwordAccountService.matches(uid, password.getCurrentPassword())) {
            throw new AccountResultStatusException(AccountResultStatus.PASSWORD_INVALID, "uid: " + uid);
        }

        passwordAccountService.change(uid, password.getNewPassword());
    }

    // todo 삭제
}
