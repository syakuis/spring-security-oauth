package io.github.syakuis.oauth2.account.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-05
 */
@Repository
@RequiredArgsConstructor
public class AccountQueryDsl implements AccountRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByUsername(String username) {
        return jpaQueryFactory.selectFrom(QAccountEntity.accountEntity)
            .where(QAccountEntity.accountEntity.deleted.isNotNull().and(QAccountEntity.accountEntity.username.eq(username))).limit(1).fetchCount() > 0;
    }
}
