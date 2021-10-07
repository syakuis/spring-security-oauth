package io.github.syakuis.oauth2.authorization.api.main.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MainRestControllerTest {
    @Autowired
    private MockMvc mvc;

    private static final String clientId = "clientId";
    private static final String clientSecret = "1234";
    private static final String testUsername = "test";
    private static final String testPassword = "1234";

    /**
     * curl -u clientId:1234 http://localhost:8080/oauth/token -d  "grant_type=password&username=test&password=1234"
     * @return
     */
    private Map<String, Object> obtainAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:8080/oauth/token")
            .queryParam("grant_type", "password")
            .queryParam("username", testUsername)
            .queryParam("password", testPassword)
            .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
            .exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), String.class);

        log.debug(response.getBody());
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response.getBody());
    }

    private Map<String, Object> obtainRefreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:8080/oauth/token")
            .queryParam("grant_type", "refresh_token")
            .queryParam("refresh_token", refreshToken)
            .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
            .exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), String.class);

        log.debug(response.getBody());
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response.getBody());
    }

    @Test
    void accessToken() throws Exception {
        Map<String, Object> oauth = obtainAccessToken();
        String accessToken = oauth.get("access_token").toString();
        log.debug(accessToken);
        assertNotNull(accessToken);

        this.mvc.perform(get("/api/main")
            .header(Headers.AUTHORIZATION_STRING, OAuth2AccessToken.BEARER_TYPE + " " + accessToken))
            .andExpect(status().isOk());
    }


    @Test
    void refreshToken() throws Exception {
        Map<String, Object> oauth = obtainAccessToken();
        String accessToken = oauth.get("access_token").toString();
        String refreshToken = oauth.get("refresh_token").toString();
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        log.debug("accessToken: {}", accessToken);
        log.debug("refreshToken: {}", refreshToken);

        Map<String, Object> oauth2 = obtainRefreshToken(refreshToken);

        String accessToken2 = oauth2.get("access_token").toString();
        String refreshToken2 = oauth2.get("refresh_token").toString();

        assertNotNull(accessToken2);
        assertNotNull(refreshToken2);

        this.mvc.perform(get("/api/main")
            .header(Headers.AUTHORIZATION_STRING, OAuth2AccessToken.BEARER_TYPE + " " + accessToken2))
            .andExpect(status().isOk());
    }

    @Test
    void accessDined() throws Exception {
        mvc.perform(get("/api/main")).andExpect(status().isUnauthorized());
    }

}