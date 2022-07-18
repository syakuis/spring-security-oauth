package io.github.syakuis.oauth2.authorization.application;

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
import io.github.syakuis.oauth2.authorization.application.restdocs.AccessTokenField;
import io.github.syakuis.oauth2.authorization.application.restdocs.AuthorizationHeaderField;
import io.github.syakuis.oauth2.restdocs.AutoConfigureMvcRestDocs;
import io.github.syakuis.oauth2.restdocs.constraints.DescriptorCollectors;
import io.github.syakuis.oauth2.restdocs.constraints.RestDocsDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-16
 * @see org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMvcRestDocs
@Sql({"/schema/client-registration-data.sql"})
class ClientCredentialsAuthorizedGrantTypeTest {
    @Autowired
    private MockMvc mvc;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    private final String restdocsPath = "authorization/client-credentials-authorized-grant-type/{method-name}";

    private final RestDocsDescriptor descriptor = new RestDocsDescriptor(AccessTokenField.values());

    @Test
    void token() throws Exception {
        mvc.perform(post("/oauth2/token")
                .param("grant_type", GrantType.CLIENT_CREDENTIALS.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(clientId, clientSecret))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.uid").doesNotExist())
            .andExpect(jsonPath("$.name").doesNotExist())
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description(AuthorizationHeaderField.basicAuthentication.getDescription()),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                requestParameters(
                    descriptor.of(AccessTokenField.grant_type)
                        .collect(DescriptorCollectors::parameterDescriptor)
                ),

                responseFields(
                    descriptor.of(
                        AccessTokenField.access_token,
                        AccessTokenField.token_type,
                        AccessTokenField.expires_in,
                        AccessTokenField.scope,
                        AccessTokenField.jti
                    ).collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }
}
