package com.academy.auth.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@ToString
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username; // appName:username
    private String password;
    @Indexed
    private String email;
    private String role;
    private String groupId;
    private boolean enabled;
    private String appName;

}
