package io.github.syakuis.identity.authorization.token.application;

import java.util.Map;
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

/**
 * @author Seok Kyun. Choi.
 * @since 2021-09-16
 */
@Slf4j
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AccessTokenService {
    WebTestClient webTestClient;
    String username;
    String password;
    String clientId;
    String clientSecret;

    public Map<String, Object> obtain(){
        return this.obtain(clientId, clientSecret, username, password);
    }

    public Map<String, Object> obtain(String clientId, String clientSecret, String username, String password) {

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

        log.debug("{}", resultString);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString);
    }

    public String accessToken(Map<String, Object> data) {
        return (String) data.get("access_token");
    }

    public String refreshToken(Map<String, Object> data) {
        return (String) data.get("refresh_token");
    }
}
