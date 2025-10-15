package org.tommap.tomuserloginrestapis.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.model.response.ApiResponse;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String msg = (accessDeniedException != null && accessDeniedException.getMessage() != null)
                ? accessDeniedException.getMessage()
                : "Access denied";

        var errorResp = ApiResponse.error(FORBIDDEN.value(), msg);
        var jsonResp = objectMapper.writeValueAsString(errorResp);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(FORBIDDEN.value());
        response.getWriter().write(jsonResp);
    }
}
