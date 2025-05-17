package com.ChatApp.ChatAppSecurity.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("Request Comes In From Uri : {}", request.getRequestURI());

        // Extract token from header
        String jwtToken = parseToken(request);


        // Only validate token if it exists
        if (jwtToken != null) {
            try {
                jwtUtils.validateAccessToken(jwtToken);
                String userName = jwtUtils.getUserNameFromJwtToken(jwtToken);
                UserDetails user = userDetailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            } catch (JwtUtils.TokenExpiredException e) {
                logger.debug("Token is expired: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "expired_token", e.getMessage());
            } catch (JwtUtils.TokenInvalidException e) {
                logger.debug("Token validation failed: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "invalid_token", e.getMessage());
            } catch (Exception e) {
                logger.error("Unexpected error in token filter: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "token_error", "Unexpected token error");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String errorCode, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status);
        errorResponse.put("error_code", errorCode);
        errorResponse.put("message", message);
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }


    public String parseToken(HttpServletRequest request){
        logger.debug("Parsing request to give Token From Header");
        return jwtUtils.getJwtTokenFromHeader(request);
    }
}


