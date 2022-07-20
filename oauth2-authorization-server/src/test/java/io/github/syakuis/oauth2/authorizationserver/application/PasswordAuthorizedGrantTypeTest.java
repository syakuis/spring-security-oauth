package io.github.syakuis.oauth2.authorizationserver.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nimbusds.oauth2.sdk.GrantType;
import io.github.syakuis.oauth2.authorizationserver.application.restdocs.AccessTokenField;
import io.github.syakuis.oauth2.authorizationserver.application.restdocs.AuthorizationHeaderField;
import io.github.syakuis.oauth2.restdocs.AutoConfigureMvcRestDocs;
import io.github.syakuis.oauth2.restdocs.constraints.DescriptorCollectors;
import io.github.syakuis.oauth2.restdocs.constraints.RestDocsDescriptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-14
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureMvcRestDocs
@Sql({"/schema/client-registration-data.sql", "/schema/account-data.sql"})
class PasswordAuthorizedGrantTypeTest {

    @Autowired
    private MockMvc mvc;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    private final String restdocsPath = "authorization/password-authorized-grant-type/{method-name}";

    private final RestDocsDescriptor descriptor = new RestDocsDescriptor(AccessTokenField.values());

    @Test
    void token() throws Exception {
        String username = "test";
        String password = "1234";

        assertNotNull(clientId);
        assertNotNull(clientSecret);

        mvc.perform(post("/oauth2/token")
                .param("grant_type", GrantType.PASSWORD.getValue())
                .param("username", username)
                .param("password", password)
                .with(httpBasic(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description(AuthorizationHeaderField.basicAuthentication.getDescription()),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_FORM_URLENCODED),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                requestParameters(
                    descriptor.of(AccessTokenField.requestAccessToken())
                        .collect(DescriptorCollectors::parameterDescriptor)
                ),

                responseFields(
                    descriptor.of(AccessTokenField.response()).collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }
}