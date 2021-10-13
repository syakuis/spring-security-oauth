package io.github.syakuis.oauth2.authorization.account;

import io.github.syakuis.account.domain.AccountEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-13
 */
@Configuration
@EntityScan(basePackageClasses = AccountEntity.class)
public class BAccountEntity {
}
