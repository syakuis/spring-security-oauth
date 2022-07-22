package io.github.syakuis.oauth2.account.application.restdocs;

import io.github.syakuis.core.test.restdocs.constraints.Descriptor;
import lombok.Getter;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-08-25
 */
@Getter
public enum AccountField implements Descriptor {
    id("번호", false),
    username("사용자 계정", false),
    password("비밀번호", false),
    name("이름", false),
    disabled("비활성", false),
    blocked("잠금", false),
    uid("사용자 식별자", false),
    updatedOn("수정일", false),
    registeredOn("생성일", true);

    private final String description;
    private final boolean optional;

    AccountField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }
}
