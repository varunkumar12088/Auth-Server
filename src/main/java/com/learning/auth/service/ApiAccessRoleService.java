package com.learning.auth.service;

import com.learning.auth.entity.ApiAccessRole;

public interface ApiAccessRoleService {

    /**
     * Checks if the given role has access to the specified URI and method.
     *
     * @param role  the role to check
     * @param uri   the URI to check access for
     * @param method the HTTP method to check access for
     * @return true if the role has access, false otherwise
     */
    boolean hasAccess(String role, String uri, String method, String ipAddress);

    /**
     * Retrieves the ApiAccessRole for the given URI and method.
     * @param uri
     * @param method
     * @return
     */
    ApiAccessRole getApiAccessRole(String uri, String method);

}
