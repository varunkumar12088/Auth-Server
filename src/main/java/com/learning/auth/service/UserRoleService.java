package com.learning.auth.service;


import com.learning.auth.dto.UserRoleDto;

public interface UserRoleService {


    /**
     * Retrieves a user role by its name.
     *
     * @param roleName the name of the role
     * @return the found role, or null if not found
     */
    UserRoleDto getRoleByName(String roleName);


}
