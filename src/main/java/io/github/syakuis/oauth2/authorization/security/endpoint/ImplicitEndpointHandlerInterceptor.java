package io.github.syakuis.oauth2.authorization.security.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-31
 */
@Slf4j
public class ImplicitEndpointHandlerInterceptor implements HandlerInterceptor {
    private final TokenStore tokenStore;
    private final ObjectMapper objectMapper;

    public ImplicitEndpointHandlerInterceptor(TokenStore tokenStore, ObjectMapper objectMapper) {
        this.tokenStore = tokenStore;
        this.objectMapper = objectMapper;
    }

    private boolean isPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        return
            new AntPathRequestMatcher("/oauth/authorize", HttpMethod.POST.name()).matches(request) &&
                Objects.equals("true", request.getParameter("user_oauth_approval")) &&
                modelAndView.getView() instanceof RedirectView
            ;
    }

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

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (isPostHandle(request, response, modelAndView)) {
            RedirectView redirectView = (RedirectView) modelAndView.getView();
            assert redirectView != null;
            assert redirectView.getUrl() != null;
            Map<String, String> parameter = toParameters(redirectView.getUrl());
            // body = jwt access_token
            OAuth2Authentication authentication = tokenStore.readAuthentication(parameter.get("access_token"));
            OAuth2AccessToken token = tokenStore.getAccessToken(authentication);

            DefaultAuthenticationKeyGenerator defaultAuthenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
            // token_id
            String accessToken = defaultAuthenticationKeyGenerator.extractKey(authentication);
            DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
            defaultOAuth2AccessToken.setAdditionalInformation(token.getAdditionalInformation());
            defaultOAuth2AccessToken.setExpiration(token.getExpiration());
            defaultOAuth2AccessToken.setTokenType(token.getTokenType());

            Map<String, String> map = objectMapper.convertValue(defaultOAuth2AccessToken, Map.class);



            log.debug("{}", response);
            log.debug("{}", handler);
        }
    }
}
