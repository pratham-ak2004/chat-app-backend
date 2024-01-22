package com.chatapp.chatappbackend.repository;

import com.chatapp.chatappbackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{
    User findByUserId(String userId);

}
