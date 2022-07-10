package io.github.syakuis.identity.clientregistration.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("test")
class ClientRegistrationRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register() throws Exception {
        String data = objectMapper.writeValueAsString(ClientRegistrationRequestDTO.Register.builder()
            .accessTokenValidity(60000)
            .refreshTokenValidity(6000)
            .scopes(Collections.singletonList("read"))
            .build());

        this.mvc.perform(post("/oauth2/v1/client")
                .contentType(MediaType.APPLICATION_JSON).content(data))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.clientId").exists())
            .andExpect(jsonPath("$.clientSecret").exists())
            .andDo(print())
        ;
    }

}