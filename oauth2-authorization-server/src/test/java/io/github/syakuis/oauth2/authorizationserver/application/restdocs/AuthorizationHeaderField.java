package io.github.syakuis.oauth2.authorizationserver.application.restdocs;

import io.github.syakuis.core.test.restdocs.constraints.Descriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-17
 */
@RequiredArgsConstructor
@Getter
public enum AuthorizationHeaderField implements Descriptor {
    basicAuthentication("Basic Authentication 인증을 사용하며 Authorization : 'basic base64(client-id, client-secret)' 처럼 HTTP Header 에 선언하여 전달한다.", false),
    bearerToken("JWT AccessToken 을 Authorization : 'Bearer accessToken' 처럼 HTTP Header 에 선언하여 전달한다.", false);

    private final String description;
    private final boolean optional;
}
