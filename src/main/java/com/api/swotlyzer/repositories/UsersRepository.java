package com.api.swotlyzer.repositories;

import com.api.swotlyzer.models.UsersModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<UsersModel, String> {
}
