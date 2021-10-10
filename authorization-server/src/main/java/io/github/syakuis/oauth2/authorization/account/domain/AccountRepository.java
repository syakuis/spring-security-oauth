package io.github.syakuis.oauth2.authorization.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);
}
