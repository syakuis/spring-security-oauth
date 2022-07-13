package io.github.syakuis.identity.clientregistration.application;

import io.github.syakuis.identity.clientregistration.domain.ClientRegistration;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/client-registrations")
class ClientRegistrationRestController {
    final ClientRegistrationService clientRegistrationService;

    @GetMapping(path = "/{clientId}")
    ClientRegistration object(@PathVariable("clientId") String clientId) {
        return clientRegistrationService.object(clientId);
    }

    // todo 토큰 유효기간을 0으로 설정할 수 없도록 강제할 것. 초단위
    @PostMapping
    ResponseEntity<ClientRegistration> register(@Valid @RequestBody ClientRegistrationRequestBody.Register register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientRegistrationService.register(register));
    }
}
