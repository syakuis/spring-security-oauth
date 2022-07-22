package io.github.syakuis.oauth2.clientregistration.application.restdocs;

import io.github.syakuis.core.test.restdocs.constraints.Descriptor;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
@Getter
public enum ClientRegistrationField implements Descriptor {
    id("번호", false),
    clientId("클라이언트 ID", false),
    clientSecret("클라이언트 비밀키", false),
    applicationName("애플리케이션 이름", false),
    resourceId("애플리케이션 ID (,로 여러개 입력 가능) 설정된 애플리케이션에 클라이언트 정보와 인증 토큰을 공유할 수 있음", false),
    scope("공개 범위", false),
    authorizedGrantType("인증방식", true),
    webServerRedirectUri("인증 후 Redirect 주소", true),
    authority("허가된 권한", true),
    accessTokenValidity("액세스 토크 유효 시간", false),
    refreshTokenValidity("재생성 액세스 토큰 유효 시간", false),
    additionalInformation("그외 추가 정보", false),
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
