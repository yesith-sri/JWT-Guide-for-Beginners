package com.edu.yesh.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private long expirationTime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        log.info("Starting JWT token generation for user: {}", authentication.getName());

        try {
            String username = authentication.getName();
            String authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime);

            String token = Jwts.builder()
                    .subject(username)
                    .claim("username", username)
                    .claim("authorities", authorities)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey())
                    .compact();

            log.info("JWT token generated for user: {}", username);
            return token;

        } catch (Exception e) {
            log.error("Error generating JWT token: {}", e.getMessage(), e);
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return (String) claims.get("username");

        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    public String getAuthoritiesFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return (String) claims.get("authorities");

        } catch (Exception e) {
            log.error("Error extracting authorities from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            log.debug("JWT token validated successfully");
            return true;

        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (Exception e) {
            log.error("Error extracting claims: {}", e.getMessage());
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            if (claims == null) {
                return true;
            }
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    public Date getTokenExpirationDate(String token) {
        try {
            Claims claims = extractAllClaims(token);
            if (claims == null) {
                return null;
            }
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("Error getting expiration date: {}", e.getMessage());
            return null;
        }
    }

    public Date getTokenIssuedDate(String token) {
        try {
            Claims claims = extractAllClaims(token);
            if (claims == null) {
                return null;
            }
            return claims.getIssuedAt();
        } catch (Exception e) {
            log.error("Error getting issued date: {}", e.getMessage());
            return null;
        }
    }

    public long getTimeUntilExpiration(String token) {
        try {
            Date expirationDate = getTokenExpirationDate(token);
            if (expirationDate == null) {
                return -1;
            }
            long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
            return Math.max(remainingTime, -1);
        } catch (Exception e) {
            log.error("Error calculating time until expiration: {}", e.getMessage());
            return -1;
        }
    }
}
