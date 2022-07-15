package io.github.syakuis.oauth2.account.application.mapper;

import io.github.syakuis.oauth2.account.application.model.AccountCommand.Profile;
import io.github.syakuis.oauth2.account.application.model.AccountCommand.Signup;
import io.github.syakuis.oauth2.account.application.model.AccountCommand.Update;
import io.github.syakuis.oauth2.account.application.model.AccountDto;
import io.github.syakuis.oauth2.account.application.model.ProfileDto;
import io.github.syakuis.oauth2.account.domain.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-06-28
 */
@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto toDto(AccountEntity accountEntity);

    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "blocked", ignore = true)
    @Mapping(target = "password", ignore = true)
    AccountEntity fromSignup(Signup signup);

    @Mapping(target = "username", ignore = true)
    AccountEntity fromUpdate(Update update);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "blocked", ignore = true)
    AccountEntity fromProfile(Profile profile);

    ProfileDto toProfileDto(AccountEntity accountEntity);
}
