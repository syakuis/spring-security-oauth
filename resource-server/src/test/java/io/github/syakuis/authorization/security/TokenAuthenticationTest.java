package io.github.syakuis.authorization.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.wildfly.common.Assert.assertNotNull;

import io.undertow.util.Headers;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TokenAuthenticationTest {
    @Autowired
    private MockMvc mvc;

    private static final String clientId = "clientId";
    private static final String clientSecret = "1234";
    private static final String testUsername = "test";
    private static final String testPassword = "1234";

    /**
     * curl -u clientId:1234 http://localhost:8080/oauth/token -d  "grant_type=password&username=test&password=1234"
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    private Map<String, Object> obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:8080/oauth/token")
            .queryParam("grant_type", "password")
            .queryParam("username", username)
            .queryParam("password", password)
            .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
            .exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), String.class);

        log.debug(response.getBody());
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response.getBody());
    }

    /**
     * curl -H "authorization: bearer AccessToken" http://localhost:8080/auth
     * Failed to find access token for token 토큰 정보가 저장되어 있지 않아 출력되는 로그, 그후 토큰을 저장한다.
     * @throws Exception
     */
    @Test
    void accessToken() throws Exception {
        Map<String, Object> oauth = obtainAccessToken(testUsername, testPassword);
        String accessToken = oauth.get("access_token").toString();
        log.debug(accessToken);
        assertNotNull(accessToken);

        this.mvc.perform(get("/api/main")
            .header(Headers.AUTHORIZATION_STRING, "Bearer " + accessToken))
            .andExpect(status().isOk());
    }

    @Test
    void refreshToken() throws Exception {
        Map<String, Object> oauth = obtainAccessToken(testUsername, testPassword);
        String accessToken = oauth.get("access_token").toString();
        String refreshToken = oauth.get("refresh_token").toString();
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        log.debug("accessToken: {}", accessToken);
        log.debug("refreshToken: {}", refreshToken);

        ResultActions result = this.mvc.perform(post("/oauth/token")
            .with(csrf())
            .with(httpBasic(clientId,clientSecret))
            .param("grant_type", "refresh_token")
            .param("refresh_token", refreshToken))
            .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String newAccessToken = jsonParser.parseMap(resultString).get("access_token").toString();
        String newRefreshToken = jsonParser.parseMap(resultString).get("refresh_token").toString();

        assertNotNull(newAccessToken);
        assertNotNull(newRefreshToken);

        this.mvc.perform(get("/api/profile")
            .header(Headers.AUTHORIZATION_STRING, "Bearer " + newAccessToken))
            .andExpect(status().isOk());
    }

    @Test
    void unAuthorized() throws Exception {
        this.mvc.perform(get("/api/profile")).andExpect(status().isUnauthorized());
    }
}
