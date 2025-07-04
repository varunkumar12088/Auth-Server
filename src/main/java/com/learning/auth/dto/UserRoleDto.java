package com.learning.auth.dto;

import com.learning.auth.entity.UserRole;
import lombok.Data;

@Data
public class UserRoleDto {

    private String role;
    private String description;

    public UserRoleDto(UserRole userRole) {
        this.role = userRole.getRole();
        this.description = userRole.getDescription();
    }
}
