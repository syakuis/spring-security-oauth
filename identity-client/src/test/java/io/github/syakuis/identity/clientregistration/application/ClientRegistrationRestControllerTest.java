package io.github.syakuis.identity.clientregistration.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.identity.clientregistration.domain.ClientRegistrationDto;
import io.github.syakuis.identity.clientregistration.support.ClientKeyGenerator;
import io.github.syakuis.identity.configuration.BasicBeanConfiguration;
import io.github.syakuis.identity.configuration.SecurityConfiguration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @see ClientRegistrationRestController
 * @since 2022-07-12
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClientRegistrationRestController.class)
@Import({BasicBeanConfiguration.class, SecurityConfiguration.class})
class ClientRegistrationRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ClientRegistrationService clientRegistrationService;

    private final String clientId = ClientKeyGenerator.clientId();
    private final String clientSecret = ClientKeyGenerator.clientSecret();

    @BeforeEach
    void init() {
        ClientRegistrationDto clientRegistration = ClientRegistrationDto.builder()
            .id(1L)
            .clientId(clientId)
            .clientSecret(passwordEncoder.encode(clientSecret))
            .refreshTokenValidity(2000)
            .accessTokenValidity(1000)
            .registeredOn(LocalDateTime.now())
            .registeredBy("tester")
            .scopes(List.of("read"))
            .authorizedGrantTypes(List.of("password"))
            .build();

        when(clientRegistrationService.object(clientId)).thenReturn(clientRegistration);
        when(clientRegistrationService.register(any())).thenReturn(clientRegistration);
    }

    @Test
    void object() throws Exception {
        mvc.perform(get("/client-registrations/{clientId}", clientId))
            .andExpect(status().isOk())
            .andDo(print())
        ;
    }

    @Test
    void register() throws Exception {

        ClientRegistrationRequestBody.Register register = ClientRegistrationRequestBody.Register.builder()
            .authorizedGrantTypes(List.of("password"))
            .scopes(List.of("read"))
            .refreshTokenValidity(2000)
            .accessTokenValidity(1000)
            .build();

        mvc.perform(post("/client-registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(register))
            )
            .andExpect(status().isCreated())
            .andDo(print())
        ;
    }

    @Test
    void update() throws Exception {
        mvc.perform(put("/client-registrations"))
            .andExpect(status().isCreated());
    }

    @Test
    void remove() throws Exception {
        mvc.perform(delete("/client-registrations/{clientId}", ""))
            .andExpect(status().isCreated());
    }

    @Test
    void resetClientSecret() {

    }
}