package io.github.syakuis.oauth2.core.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.syakuis.oauth2.core.web.ErrorResponsePayload;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author Seok Kyun. Choi.
 * @since 2019-03-12
 */
public class OAuth2AccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(
            ErrorResponsePayload.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build().wrapper()
        ));
    }
}