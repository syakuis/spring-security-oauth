package io.github.syakuis.oauth2.authorization.security.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-31
 */
@Slf4j
class ImplicitEndpointHandlerInterceptorTest {

    private Map<String, String> toParameters(String url) {
        String[] parameters = url.split("&");

        Map<String, String> result = new LinkedHashMap<>();

        for (String parameter : parameters) {
            String name = parameter.replaceAll("(.*[#?])?([a-z0-9_]+)=([^=].*)", "$2");
            String value = parameter.replaceAll("(.*[#?])?([a-z0-9_]+)=([^=].*)", "$3");
            result.put(name, value);
        }

        return result;
    }

    private String toQueryString(Map<String, String> parameters) {
        return parameters.entrySet().stream().map(set -> set.getKey() + "=" + set.getValue())
            .collect(Collectors.joining("&"));
    }

    @Test
    void toParameterTest() {
        String url = "http://localhost#";
        String queryString = "access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiJiYTUzZWFkMS0zOGY1LTQ2ODYtYjY0Yi1hOTM1M2U3YWY4MzAiLCJhZGRpdGlvbmFsSW5mb3JtYXRpb24iOm51bGwsInVzZXJfbmFtZSI6InRlc3QiLCJzY29wZSI6WyJyZWFkIl0sIm5hbWUiOiJtYXRlcmlhbCIsImV4cCI6MTk1MTAyNzMwMSwianRpIjoiZGUwYzQ5YzItYjM1Yi00NTY1LWEyMjEtNTkyNTZjOTNmNDE4IiwiY2xpZW50X2lkIjoiNGVjYmYwY2RhNWNkNTcyNTBlY2YwZDgxYzAwMjkyNzEzYmE3MzJmMjEwMWVlOTQxNmI1YmMxNGUxYzEwOTk3NTkyMjc2NjQ4YmIxYmI4NDEifQ.fmhLvP-FT9FsR5YNtigWJI2gKjxVoQxD6QFR4_ptd60iQFmlRd3CjNjAk6BdbrttDaBKn5nUPy9vnrrpzkgeWpHvO8fyFA0X3ZAqSDEBcbISGh5QzqJZvs01eVfVDcwRO8XksXoKbO8xSiG6_UTOrDItvfg5RVAvnZCnox_rwpaQhR-CAgxDMDE4r8LefuhRjncDOeHEfG2EsncqRlskV_eyD3-t4UrCwXlFX2uVJ8oGf2xytcBxNIrGPQPKWoULiCG0CROIubgU4oCMI9KmwP4biYffZ37FszOtaOs1RdkpTDpuzkuhvg8zltJCgTXmusNLBRGLi0FjFf3_n7bMMA&token_type=bearer&expires_in=315359999&uid=ba53ead1-38f5-4686-b64b-a9353e7af830&name=material&jti=de0c49c2-b35b-4565-a221-59256c93f418";

        Map<String, String> parameters = this.toParameters(url + queryString);
        parameters.forEach((k, v) -> {
            log.debug("{}={}\n", k, v);
            assertTrue(k.matches("[^#?&=].*"));
        });

        assertEquals(queryString, toQueryString(parameters));
    }
}