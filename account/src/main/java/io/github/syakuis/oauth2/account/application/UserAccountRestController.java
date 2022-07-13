package io.github.syakuis.oauth2.account.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-09
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts/users")
public class UserAccountRestController {

    // todo 비밀번호 찾기
    // todo 계정 찾기
}
