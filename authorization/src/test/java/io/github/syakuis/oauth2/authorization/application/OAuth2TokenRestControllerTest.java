package io.github.syakuis.oauth2.authorization.application;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.syakuis.oauth2.authorization.application.restdocs.AccessTokenCheckField;
import io.github.syakuis.oauth2.authorization.application.restdocs.AccessTokenField;
import io.github.syakuis.oauth2.authorization.application.restdocs.AuthorizationHeaderField;
import io.github.syakuis.oauth2.authorization.application.restdocs.JwtKeysField;
import io.github.syakuis.oauth2.restdocs.AutoConfigureMvcRestDocs;
import io.github.syakuis.oauth2.restdocs.constraints.DescriptorCollectors;
import io.github.syakuis.oauth2.restdocs.constraints.RestDocsDescriptor;
import io.github.syakuis.oauth2.restdocs.constraints.SingleDescriptor;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-18
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureMvcRestDocs
@Sql({"/schema/client-registration-data.sql", "/schema/account-data.sql"})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class OAuth2TokenRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TokenStore tokenStore;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    private OAuth2TokenService oAuth2TokenService;

    @BeforeEach
    void init() {
        oAuth2TokenService = OAuth2TokenService.builder()
            .webTestClient(webTestClient)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .username("test")
            .password("1234")
            .build();
    }

    @Test
    void check() throws Exception {
        RestDocsDescriptor accessTokenCheckFields = new RestDocsDescriptor(AccessTokenCheckField.values());
        Map<String, Object> token = oAuth2TokenService.obtainToken();

        this.mvc.perform(post("/oauth2/token-check")
                .param("token", oAuth2TokenService.getAccessToken(token))
                .with(httpBasic(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andDo(document("authorization/token/{method-name}",
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_FORM_URLENCODED),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),

                requestParameters(
                    accessTokenCheckFields.of(
                        AccessTokenCheckField.token
                    ).collect(DescriptorCollectors::parameterDescriptor)
                ),

                responseFields(
                    accessTokenCheckFields.of().exclude(AccessTokenCheckField.token)
                        .collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }

    @Test
    void revoke() throws Exception {
        SingleDescriptor accessTokenRevokeField = new SingleDescriptor("access_token", "인증 토큰", true);
        Map<String, Object> token = oAuth2TokenService.obtainToken();

        this.mvc.perform(delete("/oauth2/token")
                .param(OAuth2AccessToken.ACCESS_TOKEN, oAuth2TokenService.getAccessToken(token))
                .with(httpBasic(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            )
            .andExpect(status().isOk())
            .andDo(document("authorization/token/{method-name}",
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_FORM_URLENCODED)
                ),

                requestParameters(
                    new RestDocsDescriptor().of(
                        accessTokenRevokeField
                    ).collect(DescriptorCollectors::parameterDescriptor)
                )
            ))
        ;

        assertNull(tokenStore.readAccessToken(oAuth2TokenService.getAccessToken(token)));
    }

    @Test
    void refreshToken() throws Exception {
        RestDocsDescriptor descriptor = new RestDocsDescriptor(AccessTokenField.values());

        Map<String, Object> token = oAuth2TokenService.obtainToken();

        this.mvc.perform(post("/oauth2/token")
                .with(httpBasic(clientId, clientSecret))
                .param("grant_type", OAuth2AccessToken.REFRESH_TOKEN)
                .param(OAuth2AccessToken.REFRESH_TOKEN, oAuth2TokenService.getRefreshToken(token))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andDo(document("authorization/token/{method-name}",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description(
                        AuthorizationHeaderField.basicAuthentication.getDescription()),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_FORM_URLENCODED),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),

                requestParameters(
                    descriptor.of(
                        AccessTokenField.requestRefreshToken()
                    ).collect(DescriptorCollectors::parameterDescriptor)
                ),

                responseFields(
                    descriptor.of(AccessTokenField.response()).collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }

    @Test
    void keys() throws Exception {
        RestDocsDescriptor jwtKeysField = new RestDocsDescriptor(JwtKeysField.values());
        FieldDescriptor[] jwtKeysFields = jwtKeysField.of(JwtKeysField.responseKeys())
            .collect(DescriptorCollectors::fieldDescriptor).toArray(FieldDescriptor[]::new);

        mvc.perform(get("/oauth2/.well-known/jwks.json").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("authorization/token/{method-name}",
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                responseFields(
                    jwtKeysField.of(JwtKeysField.keys).collect(DescriptorCollectors::fieldDescriptor)
                ).andWithPrefix("keys[].", jwtKeysFields)
            ))
        ;
    }
}