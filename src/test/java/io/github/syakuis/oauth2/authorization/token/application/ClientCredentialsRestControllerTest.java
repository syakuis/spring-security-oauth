package io.github.syakuis.oauth2.authorization.token.application;

import com.nimbusds.oauth2.sdk.GrantType;
import io.github.syakuis.oauth2.configuration.TestProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-16
 * @see org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ClientCredentialsRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestProperties props;

    private String clientId;
    private String clientSecret;

    @BeforeEach
    void init() {
        clientId = props.getClientId();
        clientSecret = props.getClientSecret();
    }

    @Test
    void accessToken() throws Exception {
        mvc.perform(post("/oauth/token")
                .param("grant_type", GrantType.CLIENT_CREDENTIALS.getValue())
                .with(httpBasic(clientId, clientSecret))
            )
            .andExpect(status().isOk())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.uid").doesNotExist())
            .andExpect(jsonPath("$.name").doesNotExist())
            .andDo(print())
        ;
    }
}
