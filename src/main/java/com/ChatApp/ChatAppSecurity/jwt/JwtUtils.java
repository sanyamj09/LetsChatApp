package com.ChatApp.ChatAppSecurity.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    public class TokenValidationException extends RuntimeException {
        public TokenValidationException(String message) {
            super(message);
        }
    }

    public class TokenExpiredException extends TokenValidationException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }

    public class TokenInvalidException extends TokenValidationException {
        public TokenInvalidException(String message) {
            super(message);
        }
    }

    @Value("${spring.app.accessTokenExpirationTime}")
    private int accessTokenExpirationTime;
    @Value("${spring.app.secretKey}")
    private String secretKey;
    @Value("${spring.app.refreshTokenExpirationTime}")
    private Long refreshTokenExpirationTime;
    @Value("${spring.app.refreshTokenSecretKey}")
    private String refreshKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String getJwtTokenFromHeader(HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization");
        if(jwtToken != null && jwtToken.startsWith("Bearer ")){
            return jwtToken.substring(7);
        }
        logger.debug("No Token Found IN Header.");
        return null;
    }
    public String generateAccessTokenForSignInRequest(UserDetails userDetails){
        String userName = userDetails.getUsername();
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()+accessTokenExpirationTime))
                .signWith(key())
                .compact();
    }
    public String generateRefreshTokenForSignInRequest(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder().subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()+refreshTokenExpirationTime))
                .signWith(refreshKey()).compact();
    }
    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
    public Key refreshKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
    }
    public String getUserNameFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .verifyWith((SecretKey) refreshKey()).build().parseSignedClaims(refreshToken).getPayload().getSubject();
    }
    public String getUserNameFromJwtToken(String jwtToken){
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwtToken).getPayload().getSubject();
    }
    public void validateAccessToken(String jwtToken) throws TokenValidationException {
        try {
            logger.debug("Validating JWT Token");
            if (jwtToken == null || jwtToken.trim().isEmpty()) {
                throw new TokenInvalidException("JWT token is null or empty");
            }
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwtToken);
        } catch (ExpiredJwtException e) {
            logger.error("Token is expired: {}", e.getMessage());
            throw new TokenExpiredException("Access token has expired");
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT Token: {}", e.getMessage());
            throw new TokenInvalidException("Invalid JWT token: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error validating JWT: {}", e.getMessage());
            throw new TokenInvalidException("Unexpected error validating JWT token");
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            logger.debug("Validating Refresh Token");
            Jwts.parser()
                    .verifyWith((SecretKey) refreshKey()).build().parseSignedClaims(refreshToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid Refresh Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Refresh Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Refresh Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Refresh Token claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}


