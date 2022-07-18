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
import io.github.syakuis.oauth2.clientregistration.application.restdocs.ClientRegistrationField;
import io.github.syakuis.oauth2.clientregistration.domain.ClientRegistrationDto;
import io.github.syakuis.oauth2.clientregistration.support.ClientKeyGenerator;
import io.github.syakuis.oauth2.configuration.BasicBeanConfiguration;
import io.github.syakuis.oauth2.configuration.SecurityConfiguration;
import io.github.syakuis.oauth2.restdocs.AutoConfigureMvcRestDocs;
import io.github.syakuis.oauth2.restdocs.constraints.DescriptorCollectors;
import io.github.syakuis.oauth2.restdocs.constraints.RestDocsDescriptor;
import java.time.LocalDateTime;
import java.util.List;
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
class ClientRegistrationRestControllerTest {

    private final String restdocsPath = "client-registrations/{method-name}";
    private final RestDocsDescriptor fieldHandler = new RestDocsDescriptor(ClientRegistrationField.values());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientRegistrationService clientRegistrationService;

    private final String clientId = ClientKeyGenerator.clientId();

    private ClientRegistrationDto clientRegistrationDto;

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
            .scopes(List.of("read"))
            .authorizedGrantTypes(List.of("password"))
            .build();
    }

    @Test
    void object() throws Exception {
        when(clientRegistrationService.object(clientId)).thenReturn(clientRegistrationDto);

        mvc.perform(get("/client-registrations/{clientId}", clientId)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clientSecret", nullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
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
            .authorizedGrantTypes(List.of("password"))
            .scopes(List.of("read"))
            .refreshTokenValidity(2000)
            .accessTokenValidity(1000)
            .build();

        mvc.perform(post("/client-registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(register))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.clientSecret", notNullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                requestFields(
                    fieldHandler.of(
                        ClientRegistrationField.authorizedGrantTypes,
                        ClientRegistrationField.scopes,
                        ClientRegistrationField.refreshTokenValidity,
                        ClientRegistrationField.accessTokenValidity,
                        ClientRegistrationField.resourceIds,
                        ClientRegistrationField.webServerRedirectUri,
                        ClientRegistrationField.authorities,
                        ClientRegistrationField.additionalInformation,
                        ClientRegistrationField.autoApprove
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
            .authorizedGrantTypes(List.of("password"))
            .scopes(List.of("read"))
            .refreshTokenValidity(2000)
            .accessTokenValidity(1000)
            .build();

        mvc.perform(put("/client-registrations/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(register))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clientSecret", nullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON)
                ),

                pathParameters(parameterWithName(ClientRegistrationField.clientId.name()).description(
                    ClientRegistrationField.clientId.getDescription())),

                requestFields(
                    fieldHandler.of(
                        ClientRegistrationField.authorizedGrantTypes,
                        ClientRegistrationField.scopes,
                        ClientRegistrationField.refreshTokenValidity,
                        ClientRegistrationField.accessTokenValidity,
                        ClientRegistrationField.resourceIds,
                        ClientRegistrationField.webServerRedirectUri,
                        ClientRegistrationField.authorities,
                        ClientRegistrationField.additionalInformation,
                        ClientRegistrationField.autoApprove
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
        mvc.perform(delete("/client-registrations/{clientId}", clientId))
            .andExpect(status().isOk())
            .andExpect(content().bytes(new byte[0]))
            .andDo(document(restdocsPath,
                pathParameters(parameterWithName(ClientRegistrationField.clientId.name()).description(
                    ClientRegistrationField.clientId.getDescription()))
            ))
        ;
    }

    @Test
    void refreshingClientSecret() throws Exception {
        ReflectionTestUtils.setField(clientRegistrationDto, "clientSecret", ClientKeyGenerator.clientSecret());
        ReflectionTestUtils.setField(clientRegistrationDto, "updatedOn", LocalDateTime.now());
        when(clientRegistrationService.refreshingClientSecret(any())).thenReturn(clientRegistrationDto);

        mvc.perform(patch("/client-registrations/{clientId}/refreshing-client-secrets", clientId)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clientSecret", notNullValue()))
            .andDo(document(restdocsPath,
                requestHeaders(
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