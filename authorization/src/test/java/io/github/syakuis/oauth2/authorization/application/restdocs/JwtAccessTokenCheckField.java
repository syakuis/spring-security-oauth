package io.github.syakuis.oauth2.authorization.application.restdocs;

import io.github.syakuis.oauth2.restdocs.constraints.Descriptor;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 */

@Getter
public enum JwtAccessTokenCheckField implements Descriptor {
    token("인증 토큰", false),
    user_name("사용자 이름", false),
    active("활성화 여부", false),
    client_id("클라이언트 ID", false),
    exp("토큰 만료일", false)
    ;

    private final String description;
    private final boolean optional;

    JwtAccessTokenCheckField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }


    public static Descriptor[] response() {
        return new Descriptor[]{
            user_name,
            active,
            client_id,
            exp,
            JwtAccessTokenField.uid,
            JwtAccessTokenField.additionalInformation,
            JwtAccessTokenField.scope,
            JwtAccessTokenField.name,
            JwtAccessTokenField.jti
        };
    }

}
