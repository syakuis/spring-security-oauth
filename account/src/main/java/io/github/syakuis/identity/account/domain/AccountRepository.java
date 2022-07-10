package io.github.syakuis.identity.account.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.Repository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public interface AccountRepository extends Repository<AccountEntity, Long> {
    List<AccountEntity> findAll();

    Optional<AccountEntity> findByUsername(String username);

    Optional<AccountEntity> findByUid(UUID uid);

    AccountEntity save(AccountEntity accountEntity);
}
