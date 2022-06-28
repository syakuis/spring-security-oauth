package io.github.syakuis.oauth2.account.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByUsername(String username);

    Optional<AccountEntity> findByUid(UUID uid);
}
