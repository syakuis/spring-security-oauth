package io.github.syakuis.oauth2.authorization.security.response;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-31
 */
@Slf4j
public class ImplicitEndpointHandlerInterceptor implements HandlerInterceptor {
    private final TokenStore tokenStore;

    public ImplicitEndpointHandlerInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private boolean isPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        return
            new AntPathRequestMatcher("/oauth/authorize", HttpMethod.POST.name()).matches(request) &&
                Objects.equals("true", request.getParameter("user_oauth_approval")) &&
                modelAndView.getView() instanceof RedirectView
            ;
    }

    private Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (isPostHandle(request, response, modelAndView)) {
            RedirectView redirectView = (RedirectView) modelAndView.getView();
            Map<String, String> url = splitQuery(new URL(redirectView.getUrl()));
            // body = jwt access_token
            OAuth2Authentication authentication = tokenStore.readAuthentication(url.get("access_token"));
            OAuth2AccessToken token = tokenStore.getAccessToken(authentication);

            DefaultAuthenticationKeyGenerator defaultAuthenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
            // token_id
            String accessToken = defaultAuthenticationKeyGenerator.extractKey(authentication);
            DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
            defaultOAuth2AccessToken.setAdditionalInformation(token.getAdditionalInformation());
            defaultOAuth2AccessToken.setExpiration(token.getExpiration());
            defaultOAuth2AccessToken.setTokenType(token.getTokenType());

            log.debug("{}", response);
            log.debug("{}", handler);
        }
    }
}
