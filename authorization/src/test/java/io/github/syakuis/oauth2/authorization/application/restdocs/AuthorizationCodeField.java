package io.github.syakuis.oauth2.authorization.application.restdocs;

import io.github.syakuis.oauth2.restdocs.constraints.Descriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-15
 */
@RequiredArgsConstructor
@Getter
public enum AuthorizationCodeField implements Descriptor {
    response_type("응답 형식 : code 고정", true),
    client_id("client_id", true),
    redirect_uri("인증 후 이동할 redirect uri", true),
    scope("정보 접근 범위", true),
    grant_type("인증 방식", true),
    code("인증 요청으로 발급받은 임의의 코드", true)
    ;

    private final String description;
    private final boolean optional;
}
