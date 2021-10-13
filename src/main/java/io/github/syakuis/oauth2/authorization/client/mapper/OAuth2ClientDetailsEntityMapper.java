package io.github.syakuis.oauth2.authorization.client.mapper;

import io.github.syakuis.oauth2.authorization.client.domain.OAuth2ClientDetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-11
 */
@Mapper
public interface OAuth2ClientDetailsEntityMapper {
    OAuth2ClientDetailsEntityMapper INSTANCE = Mappers.getMapper(OAuth2ClientDetailsEntityMapper.class);
    @Mapping(source = "clientSecret", target = "clientSecret")
    OAuth2ClientDetailsEntity updatePassword(String clientSecret, OAuth2ClientDetailsEntity entity);
}
