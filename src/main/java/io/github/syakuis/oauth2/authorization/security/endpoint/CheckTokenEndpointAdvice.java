package io.github.syakuis.oauth2.authorization.security.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

/**
 * @author Seok Kyun. Choi.
 * @see CheckTokenEndpoint
 * @since 2021-10-29
 */
@Slf4j
@Aspect
@Component
public class CheckTokenEndpointAdvice {
    private final TokenStore tokenStore;

    @Autowired
    public CheckTokenEndpointAdvice(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Around(value = "execution(* org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint.checkToken(..))")
    public Object checkToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        log.debug("===> result: {}", result);

        return result;
    }
}
