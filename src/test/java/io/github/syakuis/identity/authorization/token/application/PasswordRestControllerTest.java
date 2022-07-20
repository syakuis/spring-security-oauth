package io.github.syakuis.oauth2.authorizationserver.token.application;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nimbusds.oauth2.sdk.GrantType;
import io.github.syakuis.oauth2.configuration.TestProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-14
 * @see org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PasswordRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestProperties props;

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
            .andDo(print())
        ;
    }
}
