package io.github.syakuis.oauth2.test.wiremock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-04-27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigureWireMock
@TestPropertySource(properties = {
    "spring.security.oauth2.authorization-server.host=http://localhost:${wiremock.server.port}"
})
public @interface OAuth2WireMockTest {
    @AliasFor(annotation = AutoConfigureWireMock.class, attribute = "port")
    int port() default 0;

    @AliasFor(annotation = AutoConfigureWireMock.class, attribute = "httpsPort")
    int httpsPort() default -1;

    // todo 설정하면 json 파일을 읽지 못함.
    @AliasFor(annotation = AutoConfigureWireMock.class, attribute = "stubs")
    String[] stubs() default { "" };

    @AliasFor(annotation = AutoConfigureWireMock.class, attribute = "files")
    String[] files() default { "" };
}
