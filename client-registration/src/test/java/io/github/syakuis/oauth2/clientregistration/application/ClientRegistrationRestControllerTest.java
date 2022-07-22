package io.github.syakuis.oauth2.clientregistration.application;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.core.test.restdocs.AutoConfigureMvcRestDocs;
import io.github.syakuis.core.test.restdocs.constraints.DescriptorCollectors;
import io.github.syakuis.core.test.restdocs.constraints.RestDocsDescriptor;
import io.github.syakuis.oauth2.clientregistration.application.restdocs.ClientRegistrationField;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationDto;
import io.github.syakuis.oauth2.clientregistration.support.ClientKeyGenerator;
import io.github.syakuis.oauth2.configuration.BasicBeanConfiguration;
import io.github.syakuis.oauth2.configuration.SecurityConfiguration;
import io.github.syakuis.oauth2.core.AuthorizedGrantType;
import io.github.syakuis.oauth2.test.wiremock.OAuth2WireMockTest;
import java.time.LocalDateTime;
import java.util.Set;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Seok Kyun. Choi.
 * @see ClientRegistrationRestController
 * @since 2022-07-12
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = ClientRegistrationRestController.class)
@AutoConfigureMvcRestDocs
@Import({BasicBeanConfiguration.class, SecurityConfiguration.class})
@OAuth2WireMockTest
class ClientRegistrationRestControllerTest {

    private final String restdocsPath = "client-registration/{method-name}";
    private final RestDocsDescriptor fieldHandler = new RestDocsDescriptor(ClientRegistrationField.values());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientRegistrationService clientRegistrationService;

    private final String clientId = ClientKeyGenerator.clientId();

    private ClientRegistrationDto clientRegistrationDto;

    private final String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiIwNjkxNmE5Yy1iNjFhLTQ4ZjItODlkOS00YWJjZmQ4ZWM2ZmIiLCJhZGRpdGlvbmFsSW5mb3JtYXRpb24iOm51bGwsInVzZXJfbmFtZSI6InRlc3QiLCJzY29wZSI6WyJyZWFkIl0sIm5hbWUiOiLthYzsiqTtirgiLCJleHAiOjE2NTgzMDA3NjgsImp0aSI6IjVreU1HRFUxc1hhZGpxU2xKSmZqdzJyZU84WSIsImNsaWVudF9pZCI6IjI1M2MzOTA2N2Y2NTk2Yjk4ZWZlMzBkZTFlMjVhMWFlZWRhNDc0ZTAwMGM4ZmRkNTRkN2NmZTk1NTBlODFhNzBjNGQxMDVkNGNhMDYyYzQwIn0.Ux-f1beiH1-dXj9dTBep42xFy0jB4OmOfg_C8LJLUdDaeo5IfpkvjsW8a2QClxlAXKPTZr-AchN-y_ufE23DLBQZr5OELBUOMyjmuYlWGLdNEr8ujLiwwQNKal6qA6ac5DcpZILnr14yRoIGjleZ4RsKp2wtfiSimNmWOrWok6tvTHStj6OjsvqJD2JyKa-KEfxnzLXO-BFmTxWy5oEbpKBTLQuSp2NTex9HxqIfHGO1sbQxSANnuTE2wcvQoZ7hIE45JnCEDunNnSwVR1tXkAmD7Z-2dRea-FFK1XR5_8gFDoaq8DnRNPkKQdWd_R7AeBcT_KXtxWZOUg-FkV-RFQ";

    @BeforeEach
    void init() {
        this.clientRegistrationDto = ClientRegistrationDto.builder()
            .id(1L)
            .clientId(clientId)
            .clientSecret(null)
            .refreshTokenValidity(2000)
            .accessTokenValidity(1000)
            .registeredOn(LocalDateTime.now())
            .registeredBy("tester")
            .scope(Set.of("read"))
            .authorizedGrantType(Set.of(AuthorizedGrantType.password))
            .build();
    }

    @Test
    void object() throws Exception {
        when(clientRegistrationService.object(clientId)).thenReturn(clientRegistrationDto);

        mvc.perform(get("/client-registrations/{clientId}", clientId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clientSecret", nullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer accessToken"),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                pathParameters(parameterWithName(ClientRegistrationField.clientId.name()).description(
                    ClientRegistrationField.clientId.getDescription())),

                responseFields(
                    fieldHandler.of().collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }

    @Test
    void register() throws Exception {
        ReflectionTestUtils.setField(this.clientRegistrationDto, "clientSecret", ClientKeyGenerator.clientSecret());
        when(clientRegistrationService.register(any(), any())).thenReturn(clientRegistrationDto);

        ClientRegistrationRequestBody.Register register = ClientRegistrationRequestBody.Register.builder()
            .applicationName("테스트")
            .authorizedGrantType(Set.of(AuthorizedGrantType.password))
            .scope(Set.of("read"))
            .refreshTokenValidity(2000)
            .accessTokenValidity(1000)
            .webServerRedirectUri(Set.of("http://localhost"))
            .build();

        mvc.perform(post("/client-registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(register))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.clientSecret", notNullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer accessToken"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                requestFields(
                    fieldHandler.of(
                        ClientRegistrationField.applicationName,
                        ClientRegistrationField.authorizedGrantType,
                        ClientRegistrationField.scope,
                        ClientRegistrationField.refreshTokenValidity,
                        ClientRegistrationField.accessTokenValidity,
                        ClientRegistrationField.resourceId,
                        ClientRegistrationField.webServerRedirectUri,
                        ClientRegistrationField.authority,
                        ClientRegistrationField.additionalInformation
                    ).collect(DescriptorCollectors::fieldDescriptor)
                ),

                responseFields(
                    fieldHandler.of().collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }

    @Test
    void update() throws Exception {
        ReflectionTestUtils.setField(clientRegistrationDto, "updatedOn", LocalDateTime.now());
        when(clientRegistrationService.update(any(), any())).thenReturn(clientRegistrationDto);

        ClientRegistrationRequestBody.Register register = ClientRegistrationRequestBody.Register.builder()
            .applicationName("테스트")
            .authorizedGrantType(Set.of(AuthorizedGrantType.password))
            .scope(Set.of("read"))
            .refreshTokenValidity(2000)
            .accessTokenValidity(1000)
            .webServerRedirectUri(Set.of("http://localhost"))
            .build();

        mvc.perform(put("/client-registrations/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(register))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clientSecret", nullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer accessToken"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                pathParameters(parameterWithName(ClientRegistrationField.clientId.name()).description(
                    ClientRegistrationField.clientId.getDescription())),

                requestFields(
                    fieldHandler.of(
                        ClientRegistrationField.applicationName,
                        ClientRegistrationField.authorizedGrantType,
                        ClientRegistrationField.scope,
                        ClientRegistrationField.refreshTokenValidity,
                        ClientRegistrationField.accessTokenValidity,
                        ClientRegistrationField.resourceId,
                        ClientRegistrationField.webServerRedirectUri,
                        ClientRegistrationField.authority,
                        ClientRegistrationField.additionalInformation
                    ).collect(DescriptorCollectors::fieldDescriptor)
                ),

                responseFields(
                    fieldHandler.of().collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }

    @Test
    void remove() throws Exception {
        mvc.perform(
                delete("/client-registrations/{clientId}", clientId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andExpect(content().bytes(new byte[0]))
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer accessToken")
                ),

                pathParameters(parameterWithName(ClientRegistrationField.clientId.name()).description(
                    ClientRegistrationField.clientId.getDescription()))
            ))
        ;
    }

    @Test
    void resetClientSecret() throws Exception {
        ReflectionTestUtils.setField(clientRegistrationDto, "clientSecret", ClientKeyGenerator.clientSecret());
        ReflectionTestUtils.setField(clientRegistrationDto, "updatedOn", LocalDateTime.now());
        when(clientRegistrationService.resetClientSecret(any())).thenReturn(clientRegistrationDto);

        mvc.perform(patch("/client-registrations/{clientId}/reset-client-secrets", clientId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clientSecret", notNullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer accessToken"),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                pathParameters(parameterWithName(ClientRegistrationField.clientId.name()).description(
                    ClientRegistrationField.clientId.getDescription())),

                responseFields(
                    fieldHandler.of().collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }
}