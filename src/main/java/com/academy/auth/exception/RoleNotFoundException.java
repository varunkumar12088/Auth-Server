package com.academy.auth.exception;

public class RoleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RoleNotFoundException(String roleName) {
        super("Role not found: " + roleName);
    }

    public RoleNotFoundException(String roleName, Throwable cause) {
        super("Role not found: " + roleName, cause);
    }
}
