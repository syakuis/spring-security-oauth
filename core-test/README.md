### Sample Code

```java

@Getter
public enum ClientRegistrationField implements FieldSpec {
    id("번호", false),
    clientId("클라이언트 ID", false),
    clientSecret("클라이언트 비밀키", false),
    resourceIds("자원 ID", false),
    scopes("공개 범위", false),
    authorizedGrantTypes("인증방식", true),
    webServerRedirectUri("인증 후 Redirect 주소", true),
    authorities("허가된 권한", true),
    accessTokenValidity("액세스 토크 유효 시간", false),
    refreshTokenValidity("재생성 액세스 토큰 유효 시간", false),
    additionalInformation("그외 추가 정보", false),
    autoApprove("autoApprove", false),
    registeredOn("등록일", false),
    registeredBy("등록자", false),
    updatedOn("수정일", false)
    ;

    private final String description;
    private final boolean optional;

    ClientRegistrationField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }
}
```

```java
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = ClientRegistrationRestController.class)
@AutoConfigureMvcRestDocs
@Import({BasicBeanConfiguration.class, SecurityConfiguration.class})
class ClientRegistrationRestControllerTest {

    private final String restdocsPath = "client-registrations/{method-name}";
    private final RestDocsFieldHandler fieldHandler = new RestDocsFieldHandler(ClientRegistrationField.values());

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
                    fieldHandler.payload().collect()
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
                    fieldHandler.payload(
                        ClientRegistrationField.authorizedGrantTypes.name(),
                        ClientRegistrationField.scopes.name(),
                        ClientRegistrationField.refreshTokenValidity.name(),
                        ClientRegistrationField.accessTokenValidity.name(),
                        ClientRegistrationField.resourceIds.name(),
                        ClientRegistrationField.webServerRedirectUri.name(),
                        ClientRegistrationField.authorities.name(),
                        ClientRegistrationField.additionalInformation.name(),
                        ClientRegistrationField.autoApprove.name()
                    ).collect()
                ),

                responseFields(
                    fieldHandler.payload().collect()
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
                    fieldHandler.payload(
                        ClientRegistrationField.authorizedGrantTypes.name(),
                        ClientRegistrationField.scopes.name(),
                        ClientRegistrationField.refreshTokenValidity.name(),
                        ClientRegistrationField.accessTokenValidity.name(),
                        ClientRegistrationField.resourceIds.name(),
                        ClientRegistrationField.webServerRedirectUri.name(),
                        ClientRegistrationField.authorities.name(),
                        ClientRegistrationField.additionalInformation.name(),
                        ClientRegistrationField.autoApprove.name()
                    ).collect()
                ),

                responseFields(
                    fieldHandler.payload().collect()
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
                    fieldHandler.payload().collect()
                )
            ))
        ;
    }
}
```