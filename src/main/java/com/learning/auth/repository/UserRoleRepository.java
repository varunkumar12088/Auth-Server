package com.learning.auth.repository;

import com.learning.auth.entity.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends MongoRepository<UserRole, String> {

    /**
     * Finds a user role by its name.
     *
     * @param roleName the name of the role
     * @return the found role, or null if not found
     */
    UserRole findByRole(String roleName);
}
