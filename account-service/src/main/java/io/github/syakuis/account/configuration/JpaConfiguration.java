package io.github.syakuis.account.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-12
 */
@Configuration
@ComponentScan(basePackages = "io.github.syakuis.account.domain")
public class JpaConfiguration {

}
