package io.github.syakuis.oauth2.authorization.oauth2.application.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2/v1/client")
class OAuth2ClientDetailsRestController {

    final OAuth2ClientDetailsService oAuthClientDetailsService;

    // todo 토큰 유효기간을 0으로 설정할 수 없도록 강제할 것. 초단위
    @PostMapping
    @ResponseStatus
    ResponseEntity<ClientDetailsResponseDTO.Body> register(@RequestBody ClientDetailsRequestDTO.Register register) {
        return ResponseEntity.ok(oAuthClientDetailsService.register(register));
    }

    // TODO 데이터 삭제

    // TODO 데이터 조회
}
