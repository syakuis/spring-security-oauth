package io.github.syakuis.oauth2.authorization.client.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.oauth2.authorization.client.application.OAuth2ClientDetailsRequestDTO;
import java.util.Collections;
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
 * @since 2021-05-21
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OAuthClientDetailsEntityRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void clientRegister() throws Exception {
        String data = objectMapper.writeValueAsString(OAuth2ClientDetailsRequestDTO.Register.builder()
            .accessTokenValidity(60000)
            .refreshTokenValidity(6000)
            .scopes(Collections.singletonList("read"))
            .build());
        this.mvc.perform(post("/oauth2/v1/client").contentType(MediaType.APPLICATION_JSON).content(data))
            .andExpect(status().isOk());
    }

}