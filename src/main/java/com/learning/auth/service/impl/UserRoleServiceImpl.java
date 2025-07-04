package com.learning.auth.service.impl;

import com.learning.auth.dto.UserRoleDto;
import com.learning.auth.exception.RoleNotFoundException;
import com.learning.auth.repository.UserRoleRepository;
import com.learning.auth.service.UserRoleService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleServiceImpl.class);
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRoleDto getRoleByName(String roleName) {
        LOGGER.debug("Retrieving role by name: {}", roleName);
        // Validate the role name
        if (StringUtils.isBlank(roleName)){
            LOGGER.warn("Role name is null or empty");
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        // Fetch the role from the repository
        var userRole = userRoleRepository.findByRole(roleName);
        if(ObjectUtils.isEmpty(userRole)){
            LOGGER.warn("Role not found for name: {}", roleName);
            throw new RoleNotFoundException(roleName);
        }
        return new UserRoleDto(userRole);
    }

}
