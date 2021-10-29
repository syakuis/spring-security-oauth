package io.github.syakuis.oauth2.configuration;

import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.*;

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
