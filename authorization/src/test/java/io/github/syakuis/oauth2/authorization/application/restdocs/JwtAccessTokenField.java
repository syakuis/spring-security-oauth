package io.github.syakuis.oauth2.authorization.application.restdocs;

import io.github.syakuis.oauth2.restdocs.constraints.Descriptor;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 */

@Getter
public enum JwtAccessTokenField implements Descriptor {
    grant_type("인증 방식", false),
    username("사용자 이름", false),
    password("비밀번호", false),
    access_token("인증 토큰", false),
    refresh_token("재인증 토큰", false),
    token_type("토큰 형식", false),
    scope("인증 범위", false),
    uid("사용자 식별자", false),
    name("이름", false),
    additionalInformation("사용자 추가 정보", false),
    jti("jti", false),
    expires_in("토큰 만료일", false)
    ;

    private final String description;
    private final boolean optional;

    JwtAccessTokenField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }

    public static Descriptor[] requestAccessToken() {
        return new Descriptor[]{
            grant_type,
            username,
            password
        };
    }

    public static Descriptor[] requestRefreshToken() {
        return new Descriptor[]{
            grant_type,
            refresh_token
        };
    }

    public static Descriptor[] response() {
        return new Descriptor[]{
            access_token,
            refresh_token,
            token_type,
            scope,
            uid,
            name,
            additionalInformation,
            jti,
            expires_in
        };
    }
}
