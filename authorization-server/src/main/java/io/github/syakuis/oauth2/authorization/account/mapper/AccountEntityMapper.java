package io.github.syakuis.oauth2.authorization.account.mapper;

import io.github.syakuis.oauth2.authorization.account.domain.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-10
 */
@Mapper
public interface AccountEntityMapper {
    AccountEntityMapper INSTANCE = Mappers.getMapper(AccountEntityMapper.class);

    @Mapping(source = "password", target = "password")
    AccountEntity updatePassword(String password, AccountEntity accountEntity);
}
