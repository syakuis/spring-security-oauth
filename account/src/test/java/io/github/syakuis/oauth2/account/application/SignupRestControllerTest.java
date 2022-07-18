package io.github.syakuis.oauth2.account.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.oauth2.account.application.model.AccountCommand.Signup;
import io.github.syakuis.oauth2.account.application.model.AccountDto;
import io.github.syakuis.oauth2.account.application.restdocs.AccountField;
import io.github.syakuis.oauth2.account.application.service.SignupAccountService;
import io.github.syakuis.oauth2.configuration.BasicBeanConfiguration;
import io.github.syakuis.oauth2.configuration.SecurityConfiguration;
import io.github.syakuis.oauth2.restdocs.AutoConfigureMvcRestDocs;
import io.github.syakuis.oauth2.restdocs.constraints.DescriptorCollectors;
import io.github.syakuis.oauth2.restdocs.constraints.RestDocsDescriptor;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-13
 */
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = SignupRestController.class)
@AutoConfigureMvcRestDocs
@Import({BasicBeanConfiguration.class, SecurityConfiguration.class})
class SignupRestControllerTest {

    private final String restdocsPath = "account/{method-name}";
    private final RestDocsDescriptor descriptor = new RestDocsDescriptor(AccountField.values());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignupAccountService signupAccountService;

    private AccountDto accountDto;

    @BeforeEach
    void init() {
        accountDto = AccountDto.builder()
            .id(1L)
            .blocked(false)
            .disabled(false)
            .name("홍길동")
            .username("honggildong")
            .registeredOn(LocalDateTime.now())
            .uid(UUID.randomUUID())
            .build();
    }

    @Test
    void signup() throws Exception {
        when(signupAccountService.signup(any())).thenReturn(accountDto);

        Signup signup = Signup.builder()
            .name(accountDto.getName())
            .username(accountDto.getUsername())
            .password("123456")
            .build();

        mvc.perform(post("/accounts/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup))
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.password").doesNotExist())
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                requestFields(
                    descriptor.of(
                        AccountField.username,
                        AccountField.password,
                        AccountField.name
                    ).collect(DescriptorCollectors::fieldDescriptor)
                ),

                responseFields(
                    descriptor.of().exclude(AccountField.password).collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }

    @Test
    void duplicateUsername() throws Exception {
        when(signupAccountService.usernameExists(any())).thenReturn(false);

        mvc.perform(get("/accounts/signup/duplicate-username")
                .param("username", "march")
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isOk())
            .andDo(document(
                restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.TEXT_PLAIN)
                ),
                requestParameters(
                    descriptor.of(AccountField.username).collect(DescriptorCollectors::parameterDescriptor)
                )
            ))
        ;
    }
}