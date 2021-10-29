package io.github.syakuis.oauth2.authorization.token.application;

import io.github.syakuis.oauth2.authorization.token.model.OAuth2UserDetails;
import io.github.syakuis.oauth2.configuration.TestProperties;
import org.codehaus.plexus.util.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-16
 * @see org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
 * @see org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ImplicitRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestProperties props;

    private OAuth2UserDetails oAuth2UserDetails;
    private String clientId;

    @BeforeEach
    void init() {
        this.oAuth2UserDetails = OAuth2UserDetails.builder()
            .username(props.getUsername())
            .uid(UUID.randomUUID())
            .name("material")
            .build();

        clientId = props.getClientId();
    }

    @Test
    void accessToken() throws Exception {
        String redirectUri = "http://localhost";

        // cofirm_access 페이지 호출
        MvcResult authorize = mvc.perform(post("/oauth/authorize")
                .param("response_type", "token")
                .param("client_id", clientId)
                .param("redirect_uri", redirectUri)
                .param("scope", "read")
                .with(user(oAuth2UserDetails))
            )
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("/oauth/confirm_access"))
            .andReturn();

        ModelAndView modelAndView = authorize.getModelAndView();
        assertNotNull(modelAndView);

        // 액세스 토큰 발급
        MvcResult result = mvc.perform(post("/oauth/authorize")
                .flashAttrs(modelAndView.getModel())
                .param("user_oauth_approval", "true") // 사용자가 직접 허용한다.
                .param("scope.read", "true") // 사용자가 직접 허용한다.
                .with(user(oAuth2UserDetails))
            )
            .andExpect(status().is3xxRedirection())
            .andReturn();

        String redirectUrl = result.getResponse().getRedirectedUrl();

        assertTrue(StringUtils.contains(redirectUrl, "access_token="));
        assertTrue(StringUtils.contains(redirectUrl, "uid="));
        assertTrue(StringUtils.contains(redirectUrl, "name="));
        assertTrue(StringUtils.contains(redirectUrl, "jti="));
    }
}
