package io.github.syakuis.oauth2.authorization.application;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-14
 */

//@FrameworkEndpoint
@RestController
public class AccessTokenRestController {
    @GetMapping("/a")
    public ModelAndView root(Map<String, Object> model) {
        return new ModelAndView("forward:/a/test", model);
    }

    @RequestMapping("/a/test")
    public String sub(Map<String, Object> model) {
        return "ok";
    }
//    @Resource(name = "tokenServices")
//    private AuthorizationServerTokenServices tokenServices;
//
//    @DeleteMapping("/oauth/token")
//    @ResponseStatus(HttpStatus.OK)
//    public void revoke(HttpServletRequest request) {
//        BearerTokenExtractor bearerTokenExtractor = new BearerTokenExtractor();
//        Authentication authentication = bearerTokenExtractor.extract(request);
//        if (authentication != null && authentication.getPrincipal() != null) {
//            ((ConsumerTokenServices) tokenServices).revokeToken(authentication.getPrincipal().toString());
//        }
//    }
}
