package org.tommap.tomuserloginrestapis.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tommap.tomuserloginrestapis.shared.JwtUtils;

import java.io.IOException;

import static org.tommap.tomuserloginrestapis.constant.SecurityConstants.AUTHORIZATION_HEADER;
import static org.tommap.tomuserloginrestapis.constant.SecurityConstants.BEARER_TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwt = getJwtFromRequest(request);

        if (null != jwt) {
            var claims = jwtUtils.extractClaimsIfValid(jwt);

            if (null != claims) {
                var username = claims.get("username", String.class);
                var authorities = claims.get("authorities", String.class);

                var authentication = new UsernamePasswordAuthenticationToken(
                        username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        var bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        return (null != bearerToken && bearerToken.startsWith(BEARER_TOKEN_PREFIX))
                ? bearerToken.substring(7)
                : null;
    }
}
