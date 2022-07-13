package io.github.syakuis.oauth2.account.application;

import io.github.syakuis.oauth2.account.application.service.ManagerAccountService;
import javax.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-09
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/accounts")
public class UserAccountRestController {
    private final ManagerAccountService basicAccountService;

    @PermitAll
    @GetMapping(value = "/duplicate-username", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> duplicateUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(String.valueOf(basicAccountService.isExistsUsername(username)));
    }

    // todo 본인 인증, 메일 인증 (다른 컨트롤러에서 할것)
    // todo 비밀번호 찾기
    // todo 계정 찾기
}
