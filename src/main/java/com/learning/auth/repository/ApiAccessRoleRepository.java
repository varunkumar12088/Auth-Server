package com.learning.auth.repository;

import com.learning.auth.entity.ApiAccessRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiAccessRoleRepository extends MongoRepository<ApiAccessRole, String> {

    ApiAccessRole findByUriAndMethod(String uri, String method);

}
