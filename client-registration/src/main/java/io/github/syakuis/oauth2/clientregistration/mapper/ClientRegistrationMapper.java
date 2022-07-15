package io.github.syakuis.oauth2.clientregistration.mapper;

import io.github.syakuis.oauth2.clientregistration.application.ClientRegistrationRequestBody.Register;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationDto;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationEntity;
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

    ClientRegistrationEntity register(String clientId, String clientSecret, String registeredBy, Register register);

    @Mapping(target = "clientSecret", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "registeredBy", ignore = true)
    ClientRegistrationEntity update(Register register);

    @Mapping(target = "clientSecret", ignore = true)
    ClientRegistrationDto toDto(ClientRegistrationEntity clientRegistrationEntity);
    @Mapping(target = "clientSecret", source = "clientSecret")
    ClientRegistrationDto toDto(ClientRegistrationEntity clientRegistrationEntity, String clientSecret);
}
