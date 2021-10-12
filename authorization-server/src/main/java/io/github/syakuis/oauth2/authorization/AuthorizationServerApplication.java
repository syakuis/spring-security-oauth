package io.github.syakuis.oauth2.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"io.github.syakuis.oauth2.authorization", "io.github.syakuis.account.domain"})
@EnableJpaRepositories(basePackages = {"io.github.syakuis.oauth2.authorization", "io.github.syakuis.account.domain"})
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
}
