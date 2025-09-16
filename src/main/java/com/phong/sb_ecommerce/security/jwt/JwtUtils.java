package com.phong.sb_ecommerce.security.jwt;

import com.phong.sb_ecommerce.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.secretKey}")
    String jwtSecret;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;


    // getting token from header
    public String getJwtTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


    // getting token from cookies
    public String getJwtTokenFromCookie(HttpServletRequest request) {
        Cookie cookies = WebUtils.getCookie(request, jwtCookie); // jakarta.servlet.http
        if (cookies != null) {
            return cookies.getValue();
        }
        else return null;
    }



    //generate token from username
    public String generateJwtToken(String username) {
        return Jwts.builder()
        .subject(username)
        .issuedAt(Date.from(Instant.now()))
        .expiration(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
        .signWith(generateKey())
        .compact();
    }

    public ResponseCookie generateJwtCookies(UserDetailsImpl userDetails) {
        String jwt = generateJwtToken(userDetails.getUsername());
        return ResponseCookie.from(jwtCookie, jwt)
            .path("/api")
            .maxAge(24 * 60 * 60)
            .httpOnly(false)
            .build();
    }

    public ResponseCookie getCleanJwtCookie() {

        return ResponseCookie.from(jwtCookie, null)
            .path("/api")
            .maxAge(24 * 60 * 60)
            .build();
    }


    // getting username from jwt token
    public String getUsernameFromJwtToken(String token) {
        JwtParser parser = Jwts.parser()
        .verifyWith(generateKey())
        .build();

        return parser
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    }

    // generate signing key
    public SecretKey generateKey() {
        return Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(jwtSecret)
        );
    }


    // validate jwt token
    public boolean validateJwtToken(String authToken) {
        try {
            logger.info("validateJwtToken: {}", authToken);
            JwtParser parser = Jwts.parser()
            .verifyWith(generateKey())
            .build();
            return true;
        } catch (MalformedJwtException e){
            logger.error("MalformedJwtException: {}", e.getMessage());
        } catch (ExpiredJwtException e){
            logger.error("ExpiredJwtException: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            logger.error("UnsupportedJwtException: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            logger.error("IllegalArgumentException: {}", e.getMessage());
        }

        return false;
    }
}
