package com.learning.auth.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_roles")
@Data
@ToString
public class UserRole {

    @Id
    private String id;
    @Indexed(unique = true)
    private String role;
    private int priority;
    private String description;

}
