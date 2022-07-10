package io.github.syakuis.identity.clientregistration.mapper;

import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@Mapper
public interface ClientRegistrationEntityMapper {
    ClientRegistrationEntityMapper INSTANCE = Mappers.getMapper(ClientRegistrationEntityMapper.class);
    @Mapping(source = "clientSecret", target = "clientSecret")
    ClientRegistrationEntity updatePassword(String clientSecret, ClientRegistrationEntity entity);
}
