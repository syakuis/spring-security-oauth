### RestDocs Sample Code

```java
@Getter
public enum AccountField implements Descriptor {
    id("번호", false),
    username("사용자 계정", false),
    password("비밀번호", false),
    name("이름", false),
    disabled("비활성", false),
    blocked("잠금", false),
    uid("사용자 식별자", false),
    updatedOn("수정일", false),
    registeredOn("생성일", true);

    private final String description;
    private final boolean optional;

    AccountField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }
}

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
```