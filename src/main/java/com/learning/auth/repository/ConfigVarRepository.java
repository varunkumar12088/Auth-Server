package com.learning.auth.repository;

import com.learning.auth.entity.ConfigVar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigVarRepository extends MongoRepository<ConfigVar, String> {
}
