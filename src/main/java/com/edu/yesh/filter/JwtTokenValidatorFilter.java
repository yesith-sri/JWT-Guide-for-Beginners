package com.edu.yesh.filter;

import com.edu.yesh.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenValidatorFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            log.debug("========== JWT FILTER START ==========");
            log.debug("Request URI: {}", request.getRequestURI());

            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                log.info("JWT Token is valid");

                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                log.debug("Username: {}", username);

                if (username == null) {
                    log.error("Failed to extract username");
                    filterChain.doFilter(request, response);
                    return;
                }

                String authoritiesStr = jwtTokenProvider.getAuthoritiesFromToken(jwt);
                if (authoritiesStr == null || authoritiesStr.isEmpty()) {
                    authoritiesStr = "ROLE_USER";
                }

                List<SimpleGrantedAuthority> authorities = Arrays.stream(
                                authoritiesStr.split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("✓ Authentication set for user: {}", username);
            }

        } catch (Exception e) {
            log.error("Error in JWT filter: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            return null;
        }

        if (!bearerToken.startsWith("Bearer ")) {
            return null;
        }

        return bearerToken.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/auth/");
    }
}
