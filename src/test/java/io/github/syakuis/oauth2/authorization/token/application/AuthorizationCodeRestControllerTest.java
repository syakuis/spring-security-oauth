package io.github.syakuis.oauth2.authorization.token.application;

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
import io.github.syakuis.oauth2.authorization.token.model.OAuth2UserDetails;
import io.github.syakuis.oauth2.configuration.TestProperties;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-14
 * @see org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
 * @see org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_CLASS)
class AuthorizationCodeRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestProperties props;

    private OAuth2UserDetails oAuth2UserDetails;
    private String clientId;
    private String clientSecret;

    @BeforeEach
    void init() {
        this.oAuth2UserDetails = OAuth2UserDetails.builder()
            .username(props.getUsername())
            .uid(UUID.randomUUID())
            .name("material")
            .build();

        clientId = props.getClientId();
        clientSecret = props.getClientSecret();
    }

    @Test
    void accessToken() throws Exception {
        String redirectUri = "http://localhost";

        // cofirm_access 페이지 호출
        MvcResult authorize = mvc.perform(post("/oauth/authorize")
                .param("response_type", "code")
                .param("client_id", clientId)
                .param("redirect_uri", redirectUri)
                .param("scope", "read")
                    .with(user(oAuth2UserDetails))
            )
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("/oauth/confirm_access"))
            .andDo(print())
            .andReturn()
        ;

        ModelAndView modelAndView = authorize.getModelAndView();
        assertNotNull(modelAndView);

        // 사용자 허가 처리
        MvcResult result = mvc.perform(post("/oauth/authorize")
            .flashAttrs(modelAndView.getModel())
            .param("user_oauth_approval", "true") // 사용자가 직접 허용한다.
            .param("scope.read", "true") // 사용자가 직접 허용한다.
                .with(user(oAuth2UserDetails))
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern(redirectUri + "?code=*"))
            .andDo(print())
            .andReturn()

        ;

        String redirectUrl = result.getResponse().getRedirectedUrl();
        String code = getCode(redirectUrl);

        // 액세스 토큰 발급 요청
        mvc.perform(post("/oauth/token")
                .param("grant_type", GrantType.AUTHORIZATION_CODE.getValue())
                .param("code", code)
                .param("redirect_uri", "http://localhost")
                .with(httpBasic(clientId, clientSecret))
                .with(user(oAuth2UserDetails))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.uid").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
        ;
    }

    private String getCode(String redirectUrl) {
        Assert.hasText(redirectUrl, "입력된 값이 없습니다.");
        return redirectUrl.replaceAll("^https?://.*\\?code=(.*)", "$1");
    }
}
