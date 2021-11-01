package io.github.syakuis.oauth2.authorization.token.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.syakuis.oauth2.configuration.TestProperties;
import io.github.syakuis.oauth2.configuration.WireMockTest;
import java.util.Map;
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
class TokenManagerRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestProperties props;

    @Autowired
    private WebTestClient webTestClient;

    private AccessTokenService accessTokenService;

    @BeforeEach
    void init() {
        String username = props.getUsername();
        String password = props.getPassword();
        String clientId = props.getClientId();
        String clientSecret = props.getClientSecret();

        accessTokenService = AccessTokenService.builder()
            .webTestClient(webTestClient)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .username(username)
            .password(password)
            .build();
    }

    @Test
    @WithMockUser
    void revoke() throws Exception {
        Map<String, Object> token = accessTokenService.obtain();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenService.accessToken(token));

        mvc.perform(delete("/oauth2/v1/token").headers(headers))
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
