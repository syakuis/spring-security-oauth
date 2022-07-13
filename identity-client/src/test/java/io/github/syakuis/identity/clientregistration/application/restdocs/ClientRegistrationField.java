package io.github.syakuis.identity.clientregistration.application.restdocs;

import io.github.syakuis.identity.restdocs.FieldSpec;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
@Getter
public enum ClientRegistrationField implements FieldSpec {
    id("번호", false),
    clientId("클라이언트 ID", false),
    clientSecret("클라이언트 비밀키", false),
    resourceIds("자원 ID", false),
    scopes("공개 범위", false),
    authorizedGrantTypes("인증방식", true),
    webServerRedirectUri("인증 후 Redirect 주소", true),
    authorities("허가된 권한", true),
    accessTokenValidity("액세스 토크 유효 시간", false),
    refreshTokenValidity("재생성 액세스 토큰 유효 시간", false),
    additionalInformation("그외 추가 정보", false),
    autoApprove("autoApprove", false),
    registeredOn("등록일", false),
    registeredBy("등록자", false),
    updatedOn("수정일", false)
    ;

    private final String description;
    private final boolean optional;

    ClientRegistrationField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }
}
