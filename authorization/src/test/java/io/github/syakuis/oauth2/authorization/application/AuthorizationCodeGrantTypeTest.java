package io.github.syakuis.oauth2.authorization.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nimbusds.oauth2.sdk.GrantType;
import io.github.syakuis.oauth2.account.application.AccountUserDetailsService;
import io.github.syakuis.oauth2.account.model.AccountUserDetails;
import io.github.syakuis.oauth2.authorization.application.restdocs.AuthorizationCodeField;
import io.github.syakuis.oauth2.authorization.application.restdocs.JwtAccessTokenField;
import io.github.syakuis.oauth2.restdocs.AutoConfigureMvcRestDocs;
import io.github.syakuis.oauth2.restdocs.constraints.DescriptorCollectors;
import io.github.syakuis.oauth2.restdocs.constraints.RestDocsDescriptor;
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
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Seok Kyun. Choi.
 * @see org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
 * @see org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint
 * @since 2021-10-14
 *
 * // 액세스 토큰이 발급된 경우
 *         if (authorizeResult.getResponse().getForwardedUrl() == null) {
 *             authorize
 *                 .andExpect(status().is3xxRedirection())
 *                 .andExpect(redirectedUrlPattern(redirectUri + "?code=*"))
 *             ;
 *         }
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMvcRestDocs
@Sql({"/schema/client-registration-data.sql", "/schema/account-data.sql"})
class AuthorizationCodeGrantTypeTest {

    @Autowired
    private MockMvc mvc;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @Autowired
    private AccountUserDetailsService accountUserDetailsService;

    @Autowired
    private TokenStore tokenStore;

    private AccountUserDetails accountUserDetails;

    private final String restdocsPath = "authorization/authorization-code-authorized-grant-type/{method-name}";

    private final RestDocsDescriptor authorizationCodeDescriptor = new RestDocsDescriptor(
        AuthorizationCodeField.values());
    private final RestDocsDescriptor accessTokenDescriptor = new RestDocsDescriptor(JwtAccessTokenField.values());

    @BeforeEach
    void init() {
        // test 를 위해 인증 토큰 제거
        tokenStore.findTokensByClientIdAndUserName(clientId, "test")
            .forEach(it -> tokenStore.removeAccessToken(it));

        // test 계정 정보 조회 (로그인 된 상태로 적용 위함)
        accountUserDetails = accountUserDetailsService.loadUserByUsername("test");
    }

    @Test
    void accessToken() throws Exception {
        String redirectUri = "http://localhost";

        // cofirm_access 페이지 호출
        MvcResult authorizeResult = mvc.perform(
                post("/oauth/authorize")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("response_type", "code")
                    .param("client_id", clientId)
                    .param("redirect_uri", redirectUri)
                    .param("scope", "read")
                    // 로그인 상태에서 인증을 요청하므로 로그인 된 상태를 만듦
                    .with(user(accountUserDetails))
            )
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("/oauth/confirm_access"))
            .andExpect(content().bytes(new byte[0]))
            .andDo(print())
            .andDo(document(restdocsPath + "/authorization-code",
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_FORM_URLENCODED)
                ),

                requestParameters(
                    authorizationCodeDescriptor.of().exclude(AuthorizationCodeField.code)
                        .collect(DescriptorCollectors::parameterDescriptor)
                )
            ))
            .andReturn();

        ModelAndView modelAndView = authorizeResult.getModelAndView();
        assertNotNull(modelAndView);

        // 사용자 직접 승인 필요
        authorizeResult = mvc.perform(post("/oauth/authorize")
                .flashAttrs(modelAndView.getModel())
                .param("user_oauth_approval", "true")
                .param("scope.read", "true")
                .with(user(accountUserDetails))
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern(redirectUri + "?code=*"))
            .andReturn();

        String authorizationCode = getAuthorizationCode(authorizeResult.getResponse().getRedirectedUrl());

        // 액세스 토큰 발급 요청
        mvc.perform(post("/oauth/token")
                .param("grant_type", GrantType.AUTHORIZATION_CODE.getValue())
                .param("code", authorizationCode)
                .param("redirect_uri", redirectUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(httpBasic(clientId, clientSecret))
                .with(user(accountUserDetails))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.refresh_token").isNotEmpty())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_FORM_URLENCODED)
                ),

                requestParameters(
                    authorizationCodeDescriptor.of(
                        AuthorizationCodeField.grant_type,
                        AuthorizationCodeField.code,
                        AuthorizationCodeField.redirect_uri
                    ).collect(DescriptorCollectors::parameterDescriptor)
                ),

                responseFields(
                    accessTokenDescriptor.of(JwtAccessTokenField.response()).collect(DescriptorCollectors::fieldDescriptor)
                )
            ))
        ;
    }

    private String getAuthorizationCode(String redirectUrl) {
        Assert.hasText(redirectUrl, "입력된 값이 없습니다.");
        return redirectUrl.replaceAll("^https?://.*\\?code=(.*)", "$1");
    }
}
