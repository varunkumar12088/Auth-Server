package com.learning.auth.service.impl;

import com.learning.auth.entity.ApiAccessRole;
import com.learning.auth.exception.UnAuthException;
import com.learning.auth.repository.ApiAccessRoleRepository;
import com.learning.auth.repository.UserRoleRepository;
import com.learning.auth.service.ApiAccessRoleService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiAccessRoleServiceImpl implements ApiAccessRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAccessRoleServiceImpl.class);

    @Autowired
    private ApiAccessRoleRepository apiAccessRoleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Value("${api.access.allowed.ips}")
    private List<String> allowedIps;

    private static final Map<String, Integer> ROLE_HIERARCHY = new HashMap<>();

    @PostConstruct
    public void init(){
        userRoleRepository.findAll().forEach(userRole -> {
            ROLE_HIERARCHY.put(userRole.getRole().toUpperCase(), userRole.getPriority());
        });
    }

    @Override
    public boolean hasAccess(String role, String uri, String method, String ipAddress) {
        LOGGER.debug("Checking access for role: {}, URI: {}, method: {}, IP: {}", role, uri, method, ipAddress);
        ApiAccessRole apiAccessRole = this.getApiAccessRole(uri, method);
        if(apiAccessRole.isPublicAllowed()){
            LOGGER.debug("Public access allowed for URI: {}, method: {}", uri, method);
            if(isRoleAllowed(role, apiAccessRole.getRole())){
                return true;
            }
            return false;
        }
        if(CollectionUtils.isEmpty(allowedIps) || StringUtils.isBlank(ipAddress)) {
            LOGGER.warn("No allowed IPs configured or IP address is blank. Access denied for URI: {}, method: {}", uri, method);
            return false;
        }

        if(allowedIps.contains(ipAddress)){
            return true;
        }

        return false;
    }

    @Override
    public ApiAccessRole getApiAccessRole(String uri, String method) {
        ApiAccessRole apiAccessRole = apiAccessRoleRepository.findByUriAndMethod(uri, method);
        if (apiAccessRole != null) {
            LOGGER.debug("Found ApiAccessRole for URI: {}, method: {}", uri, method);
            return apiAccessRole;
        }
        throw new UnAuthException("Access denied for URI: " + uri + " with method: " + method + ". No matching ApiAccessRole found.");
    }

    private boolean isRoleAllowed(String userRole, String requiredRole) {
        Integer userRoleLevel = ROLE_HIERARCHY.get(userRole.toUpperCase());
        Integer requiredRoleLevel = ROLE_HIERARCHY.get(requiredRole.toUpperCase());

        if (userRoleLevel != null && requiredRoleLevel != null) {
            return userRoleLevel >= requiredRoleLevel;
        }
        return false;
    }
}
