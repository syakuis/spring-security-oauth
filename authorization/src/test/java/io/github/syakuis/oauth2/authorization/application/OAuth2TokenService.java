package io.github.syakuis.oauth2.authorization.application;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OAuth2TokenService {
    private WebTestClient webTestClient;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;

    public Map<String, Object> obtainToken(){
        return this.obtainToken(clientId, clientSecret, username, password);
    }

    public Map<String, Object> obtainToken(String clientId, String clientSecret, String username, String password) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        String resultString = webTestClient.post()
            .uri(uriBuilder -> uriBuilder.path("/oauth2/token").queryParams(params).build())
            .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class).getResponseBody().blockFirst();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString);
    }

    public String getAccessToken(Map<String, Object> data) {
        return (String) data.get("access_token");
    }

    public String getRefreshToken(Map<String, Object> data) {
        return (String) data.get("refresh_token");
    }
}
