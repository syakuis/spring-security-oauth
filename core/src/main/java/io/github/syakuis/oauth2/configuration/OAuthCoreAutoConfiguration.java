package io.github.syakuis.oauth2.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-18
 */
@Configuration(proxyBeanMethods = false)
@Import({
    BasicBeanConfiguration.class,
    QuerydslConfiguration.class,
    MessageSourceConfiguration.class,
    SecurityConfiguration.class,
})
// todo 클래스명 변경
public class OAuthCoreAutoConfiguration {
}
