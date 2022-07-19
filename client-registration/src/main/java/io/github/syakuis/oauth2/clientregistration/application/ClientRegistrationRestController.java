package io.github.syakuis.oauth2.clientregistration.application;

import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistration;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/client-registrations")
class ClientRegistrationRestController {
    final ClientRegistrationService clientRegistrationService;

    @GetMapping(path = "/{clientId}")
    ClientRegistration object(@PathVariable("clientId") String clientId) {
        return clientRegistrationService.object(clientId);
    }

    @PostMapping
    ResponseEntity<ClientRegistration> register(@Valid @RequestBody ClientRegistrationRequestBody.Register register, BearerTokenAuthentication bearerTokenAuthentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientRegistrationService.register(register, bearerTokenAuthentication.getName()));
    }

    @PutMapping(path = "/{clientId}")
    ClientRegistration update(@PathVariable("clientId") String clientId, @Valid @RequestBody ClientRegistrationRequestBody.Register register) {
        return clientRegistrationService.update(clientId, register);
    }

    @DeleteMapping(path = "/{clientId}")
    void remove(@PathVariable("clientId") String clientId) {
        clientRegistrationService.remove(clientId);
    }

    @PatchMapping(path = "/{clientId}/reset-client-secrets")
    ClientRegistration resetClientSecret(@PathVariable("clientId") String clientId) {
        return clientRegistrationService.resetClientSecret(clientId);
    }
}
