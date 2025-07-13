package com.academy.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {


    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // If already authenticated, proceed with the filter chain
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authHeader.substring(6);
                String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
                String[] parts = credentials.split(":", 2);
                if(parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];
                    Authentication authenticationWithPass = authenticationManager.authenticate(
                            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(username, password)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationWithPass);
                }

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Basic Authentication credentials");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
