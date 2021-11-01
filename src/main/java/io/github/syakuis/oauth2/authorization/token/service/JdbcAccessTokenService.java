package io.github.syakuis.oauth2.authorization.token.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
public class JdbcAccessTokenService implements AccessTokenService {
    private final JdbcTemplate jdbcTemplate;

    public String save(String authorizationId, String accessToken) {
        OAuth2AccessToken accessToken = jdbcTemplate
            .queryForObject("select token_id, token from oauth_access_token where authentication_id = ?",
                (rs, rowNum) -> SerializationUtils.deserialize(rs.getBytes(2)), authorizationId);
        Assert.notNull(accessToken, "access_token not found");

        return accessToken.getValue();
    }
}
