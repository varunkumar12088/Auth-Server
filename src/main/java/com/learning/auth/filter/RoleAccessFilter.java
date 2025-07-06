package com.learning.auth.filter;

import com.learning.auth.service.ApiAccessRoleService;
import com.learning.auth.utils.IPUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class RoleAccessFilter extends OncePerRequestFilter{

    @Autowired
    private ApiAccessRoleService apiAccessRoleService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<String> roleOpt = authentication.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .findFirst();
            if(roleOpt.isPresent()){
                String role = roleOpt.get();
                String uri = request.getRequestURI();
                String method = request.getMethod();
                String ipAddress = IPUtil.getClientIpAddress(request);
                boolean hasAccess = apiAccessRoleService.hasAccess(role, uri, method, ipAddress);
                if(!hasAccess) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Access denied for role: " + role + " to URI: " + uri + " with method: " + method);
                }
            }

        }

        filterChain.doFilter(request, response);

    }
}
