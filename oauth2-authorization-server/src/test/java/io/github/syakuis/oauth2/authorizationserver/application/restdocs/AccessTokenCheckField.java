package io.github.syakuis.oauth2.authorizationserver.application.restdocs;

import io.github.syakuis.oauth2.restdocs.constraints.Descriptor;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 */

@Getter
public enum AccessTokenCheckField implements Descriptor {
    token("인증 토큰", false),
    user_name("사용자 이름", false),
    active("활성화 여부", false),
    client_id("클라이언트 ID", false),
    exp("토큰 만료일", false),
    scope("인증 범위", false),
    aud("aud", false),
    jti("jti", false),
    uid("사용자 식별자", false),
    additionalInformation("사용자 추가 정보", false),
    name("이름", false)
    ;

    private final String description;
    private final boolean optional;

    AccessTokenCheckField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }
}
