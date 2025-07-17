package com.academy.auth.repository;

import com.academy.auth.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    List<RefreshToken> findAllByUsername(String username);

    RefreshToken findByRefreshToken(String refreshToken);
}
