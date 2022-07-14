package io.github.syakuis.oauth2.authorization.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nimbusds.oauth2.sdk.GrantType;
import io.github.syakuis.oauth2.account.application.AccountUserDetailsService;
import io.github.syakuis.oauth2.account.model.AccountUserDetails;
import io.github.syakuis.oauth2.authorization.application.restdocs.JwtAccessTokenField;
import io.github.syakuis.oauth2.restdocs.constraints.RestDocsDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_CLASS)
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

    private final String restdocsPath = "authorization/authorization-code-grant-type/{method-name}";

    private final RestDocsDescriptor descriptor = new RestDocsDescriptor(JwtAccessTokenField.values());

    @Test
    void accessToken() throws Exception {
        // 로그인 상태...
        AccountUserDetails accountUserDetails = accountUserDetailsService.loadUserByUsername("test");

        String redirectUri = "http://localhost";

        // cofirm_access 페이지 호출
        MvcResult authorize = mvc.perform(post("/oauth/authorize")
                .param("response_type", "code")
                .param("client_id", clientId)
                .param("redirect_uri", redirectUri)
                .param("scope", "read")
                .with(user(accountUserDetails))
            )
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("/oauth/confirm_access"))
            .andDo(print())
            .andReturn();

        ModelAndView modelAndView = authorize.getModelAndView();
        assertNotNull(modelAndView);
/*
        // 사용자 직접 승인 필요
        MvcResult result = mvc.perform(post("/oauth/authorize")
                .flashAttrs(modelAndView.getModel())
                .param("user_oauth_approval", "true")
                .param("scope.read", "true")
                .with(user(accountUserDetails))
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern(redirectUri + "?code=*"))
            .andDo(print())
            .andReturn();

        String redirectUrl = result.getResponse().getRedirectedUrl();
        String code = getCode(redirectUrl);

        // 액세스 토큰 발급 요청
        mvc.perform(post("/oauth/token")
                .param("grant_type", GrantType.AUTHORIZATION_CODE.getValue())
                .param("code", code)
                .param("redirect_uri", redirectUri)
                .with(httpBasic(clientId, clientSecret))
                .with(user(accountUserDetails))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andDo(print())
        ;*/
    }

    private String getCode(String redirectUrl) {
        Assert.hasText(redirectUrl, "입력된 값이 없습니다.");
        return redirectUrl.replaceAll("^https?://.*\\?code=(.*)", "$1");
    }
}
