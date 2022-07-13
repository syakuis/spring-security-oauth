package io.github.syakuis.identity.clientregistration.mapper;

import io.github.syakuis.identity.clientregistration.application.ClientRegistrationRequestBody;
import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationDto;
import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@Mapper
public interface ClientRegistrationMapper {
    ClientRegistrationMapper INSTANCE = Mappers.getMapper(ClientRegistrationMapper.class);

    @Mapping(target = "registeredBy", ignore = true)
    ClientRegistrationEntity register(String clientId, String clientSecret, ClientRegistrationRequestBody.Register register);

    @Mapping(target = "clientSecret", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "registeredBy", ignore = true)
    ClientRegistrationEntity update(ClientRegistrationRequestBody.Register register);

    @Mapping(target = "clientSecret", ignore = true)
    ClientRegistrationDto toDto(ClientRegistrationEntity clientRegistrationEntity);
    @Mapping(target = "clientSecret", source = "clientSecret")
    ClientRegistrationDto toDto(ClientRegistrationEntity clientRegistrationEntity, String clientSecret);
}
