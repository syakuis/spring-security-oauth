package io.github.syakuis.oauth2.authorization.token.application;

import com.nimbusds.oauth2.sdk.GrantType;
import io.github.syakuis.oauth2.configuration.TestProperties;
import io.github.syakuis.oauth2.configuration.WireMockTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 * @see org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WireMockTest
class PasswordRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestProperties props;

    @Autowired
    private WebTestClient webTestClient;

    private AccessTokenService accessTokenService;

    private String username;
    private String password;
    private String clientId;
    private String clientSecret;

    @BeforeEach
    void init() {
        username = props.getUsername();
        password = props.getPassword();
        clientId = props.getClientId();
        clientSecret = props.getClientSecret();

        accessTokenService = AccessTokenService.builder()
            .webTestClient(webTestClient)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .username(username)
            .password(password)
            .build();
    }

    @Test
    void accessToken() throws Exception {
        mvc.perform(post("/oauth/token")
                .param("grant_type", GrantType.PASSWORD.getValue())
                .param("username", username)
                .param("password", password)
                .with(httpBasic(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
        ;
    }

    @Test
    void refreshToken() throws Exception {
        Map<String, Object> token = accessTokenService.obtain();

        this.mvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("grant_type", "refresh_token")
                .param("refresh_token", accessTokenService.refreshToken(token))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
        ;
    }


    @Test
    void check() throws Exception {
        Map<String, Object> token = accessTokenService.obtain();

        this.mvc.perform(post("/oauth/check_token")
                .param("token", accessTokenService.accessToken(token))
                .with(httpBasic(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
        ;
    }

    @Test
    @WithMockUser
    void revoke() throws Exception {
        Map<String, Object> token = accessTokenService.obtain();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenService.accessToken(token));

        mvc.perform(delete("/oauth2/v1/token/revoke").headers(headers))
            .andExpect(status().isOk())
        ;

    }

    @Test
    void keys() throws Exception {
        mvc.perform(get("/oauth2/v1/token/keys").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
        ;
    }
}
