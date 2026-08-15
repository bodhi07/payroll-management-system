package com.payroll.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * ============================================================================
 * JWT Token Provider & Utility Component
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Generates, parses, extracts claims from, and validates JSON Web Tokens (JWT)
 * for Stateless Bearer Authentication in Spring Security.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Private helper methods conceal HMAC signing key decoding and claim parsing.
 * - Single Responsibility Principle (SOLID): Responsible solely for JWT lifecycle operations.
 * 
 * Design Patterns Used:
 * --------------------
 * - Component Pattern: Managed as a Spring Component bean.
 * - Utility Helper Pattern.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:c2VjcmV0S2V5Rm9yUGF5cm9sbE1hbmFnZW1lbnRTeXN0ZW1FbnRlcnByaXNlMjAyNkpXVDMyQnl0ZXM=}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:86400000}") // Default 24 hours
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration-ms:604800000}") // Default 7 days
    private long jwtRefreshExpirationMs;


    /**
     * Generates a signed JWT access token for an authenticated user.
     *
     * @param authentication Authentication object containing UserDetails principal
     * @return Formatted JWT token string
     */
    public String generateToken(final Authentication authentication) {
        final UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Generates a long-lived JWT refresh token for username.
     *
     * @param username User identifier
     * @return Formatted refresh JWT token string
     */
    public String generateRefreshToken(final String username) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtRefreshExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extracts username subject from valid JWT token.
     *
     * @param token JWT token string
     * @return Subject username
     */
    public String getUsernameFromToken(final String token) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Validates JWT token signature and expiration.
     *
     * @param token JWT token string
     * @return True if valid, false if expired/malformed
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token structure: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * Decodes Base64 HMAC secret key into SecretKey object.
     *
     * @return SecretKey instance for HMAC-SHA256 signing
     */
    private SecretKey getSigningKey() {
        try {
            final byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception ex) {
            return Keys.hmacShaKeyFor(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
    }

}
