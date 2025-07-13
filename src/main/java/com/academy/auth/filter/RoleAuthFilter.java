package com.academy.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class RoleAuthFilter extends OncePerRequestFilter {

    private static final String ROLE_HEADER = "Api-Role";
    private static final String USERNAME_HEADER = "Username";
    private  static final Logger LOGGER = LoggerFactory.getLogger(RoleAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            String role = request.getHeader(ROLE_HEADER);
            String username = request.getHeader(USERNAME_HEADER);
            if(StringUtils.isNotBlank(role) && StringUtils.isNotBlank(username)){
                List<GrantedAuthority> authorities = List.of(() -> role);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(token);
            }

        }
        filterChain.doFilter(request, response);
    }
}
