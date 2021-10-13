package io.github.syakuis.oauth2.authorization.token.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-16
 */
@Slf4j
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OAuthTokenService {
    WebTestClient webTestClient;
    String username;
    String password;
    String clientId;
    String clientSecret;

    public Map<String, Object> obtainAccessToken(){
        return this.obtainAccessToken(clientId, clientSecret, username, password);
    }

    public Map<String, Object> obtainAccessToken(String clientId, String clientSecret, String username, String password) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        String resultString = webTestClient.post()
            .uri(uriBuilder -> uriBuilder.path("/oauth/token").queryParams(params).build())
            .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class).getResponseBody().blockFirst();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString);
    }
}
