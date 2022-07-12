package io.github.syakuis.identity.clientregistration.mapper;

import io.github.syakuis.identity.clientregistration.application.ClientRegistrationRequestBody;
import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationDto;
import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@Mapper
public interface ClientRegistrationMapper {
    ClientRegistrationMapper INSTANCE = Mappers.getMapper(ClientRegistrationMapper.class);

    ClientRegistrationEntity register(String clientId, String clientSecret, ClientRegistrationRequestBody.Register register);
    ClientRegistrationDto toDto(ClientRegistrationEntity clientRegistrationEntity);

}
