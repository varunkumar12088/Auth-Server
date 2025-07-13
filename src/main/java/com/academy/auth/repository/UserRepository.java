package com.academy.auth.repository;

import com.academy.auth.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsernameOrEmail(String username, String email);


}
