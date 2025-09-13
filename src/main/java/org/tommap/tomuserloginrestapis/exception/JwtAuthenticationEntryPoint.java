package org.tommap.tomuserloginrestapis.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.model.response.ApiResponse;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        String msg = (null != authException && null != authException.getMessage())
                ? authException.getMessage()
                : "Authentication required";

        var errorResp = ApiResponse.error(UNAUTHORIZED.value(), msg);
        var jsonResp = objectMapper.writeValueAsString(errorResp);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(UNAUTHORIZED.value());
        response.getWriter().write(jsonResp);
    }
}
