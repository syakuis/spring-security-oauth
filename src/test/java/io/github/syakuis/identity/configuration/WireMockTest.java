package io.github.syakuis.identity.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-08
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.endpoint-url=http://localhost:${wiremock.server.port}",
    "spring.security.oauth2.resourceserver.opaquetoken.introspection-uri=http://localhost:${wiremock.server.port}/oauth/check_token"
})
public @interface WireMockTest {

}
