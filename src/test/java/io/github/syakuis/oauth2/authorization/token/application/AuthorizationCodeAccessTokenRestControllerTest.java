package io.github.syakuis.oauth2.authorization.token.application;

import io.github.syakuis.oauth2.configuration.TestProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-14
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser("test")
class AuthorizationCodeAccessTokenRestControllerTest {
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
        mvc.perform(post("/oauth/authorize")
//            .param("grant_type", "authorization_code")
            .param("response_type", "code")
            .param("client_id", clientId)
//            .param("redirect_uri", "http://localhost")
            .param("scope", "red")
        ).andExpect(status().isOk());
    }
}
