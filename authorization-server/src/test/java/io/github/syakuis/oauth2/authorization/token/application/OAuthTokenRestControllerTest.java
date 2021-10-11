package io.github.syakuis.oauth2.authorization.token.application;

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
import org.wildfly.common.Assert;

import javax.transaction.Transactional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class OAuthTokenRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebTestClient webTestClient;

    private OAuthTokenService oAuthTokenService;

    private String username = "test";
    private String password = "1234";
    private String clientId = "4ecbf0cda5cd57250ecf0d81c00292713ba732f2101ee9416b5bc14e1c10997592276648bb1bb841";
    private String clientSecret = "dUd6N3ojXSI0dGVseUwy";

    @BeforeEach
    void init() {
        oAuthTokenService = OAuthTokenService.builder()
            .webTestClient(webTestClient)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .username(username)
            .password(password)
            .build();

        log.debug("{}", oAuthTokenService);
    }

    @Test
    void accessToken() throws Exception {

        mvc.perform(post("/oauth/token")
                .param("grant_type", "password")
                .param("username", username)
                .param("password", password)
                .with(httpBasic(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
        ;
    }

    @Test
    void refreshToken() throws Exception {
        Map<String, Object> oauth = oAuthTokenService.obtainAccessToken();
        String accessToken = oauth.get("access_token").toString();
        String refreshToken = oauth.get("refresh_token").toString();

        Assert.assertNotNull(accessToken);
        Assert.assertNotNull(refreshToken);

        this.mvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("grant_type", "refresh_token")
                .param("refresh_token", refreshToken)
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
        Map<String, Object> oauth = oAuthTokenService.obtainAccessToken();
        String accessToken = oauth.get("access_token").toString();
        String refreshToken = oauth.get("refresh_token").toString();

        Assert.assertNotNull(accessToken);
        Assert.assertNotNull(refreshToken);

        this.mvc.perform(post("/oauth/check_token")
                .param("token", accessToken)
                .with(httpBasic(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
        ;
    }

    @Test
    @WithMockUser
    void revoke() throws Exception {
        Map<String, Object> oauth = oAuthTokenService.obtainAccessToken();
        String accessToken = oauth.get("access_token").toString();
        String tokenType = oauth.get("token_type").toString();

        assertNotNull(accessToken);

        mvc.perform(delete("/oauth2/v1/token/revoke").header(HttpHeaders.AUTHORIZATION, tokenType + " " + accessToken))
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