package io.github.syakuis.identity.account.application;

import io.github.syakuis.identity.account.application.model.AccountCommand;
import io.github.syakuis.identity.account.application.service.ManagerAccountService;
import io.github.syakuis.identity.account.application.service.PasswordAccountService;
import io.github.syakuis.identity.account.domain.Account;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-11
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/accounts/managers")
class ManagerAccountRestController {
    private final ManagerAccountService managerAccountService;
    private final PasswordAccountService passwordAccountService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<? extends Account> accounts() {
        return managerAccountService.accounts();
    }

    @GetMapping(path = "/{uid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Account account(@PathVariable("uid") UUID uid) {
        return managerAccountService.account(uid);
    }

    @PutMapping(path = "/{uid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Account update(@PathVariable("uid") UUID uid, @RequestBody AccountCommand.Update update) {
        return managerAccountService.update(uid, update);
    }

    @PatchMapping(path = "/{uid}/password", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@PathVariable("uid") UUID uid, @RequestBody String password) {
        passwordAccountService.change(uid, password);
    }

    // todo 탈퇴
    @DeleteMapping(path = "/{uid}")
    public ResponseEntity<Void> remove(@PathVariable("uid") UUID uid) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
