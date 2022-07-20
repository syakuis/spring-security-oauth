package io.github.syakuis.oauth2.account.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public interface AccountRepository extends Repository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);
}
