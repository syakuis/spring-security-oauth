package io.github.syakuis.oauth2.authorization;

import io.github.syakuis.account.domain.AccountDomainBasePackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackageClasses = { AuthorizationServerApplication.class, AccountDomainBasePackage.class })
@EnableJpaRepositories(basePackageClasses = { AuthorizationServerApplication.class, AccountDomainBasePackage.class})
@ComponentScan(basePackageClasses = { AuthorizationServerApplication.class, AccountDomainBasePackage.class })
public class AuthorizationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
}
