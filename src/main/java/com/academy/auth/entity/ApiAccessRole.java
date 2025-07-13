package com.academy.auth.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "api_access_role")
@Data
@ToString
public class ApiAccessRole {

    private String uri;
    private String method;
    private String role;
    private boolean isPublicAllowed;
    private String description;
}
