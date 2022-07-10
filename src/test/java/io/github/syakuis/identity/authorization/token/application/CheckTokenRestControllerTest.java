package io.github.syakuis.identity.authorization.token.application;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.syakuis.identity.configuration.TestProperties;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CheckTokenRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestProperties props;

    @Autowired
    private WebTestClient webTestClient;

    private AccessTokenService accessTokenService;

    private String clientId;
    private String clientSecret;

    @BeforeEach
    void init() {
        String username = props.getUsername();
        String password = props.getPassword();
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
}
