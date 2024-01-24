package com.chatapp.chatappbackend.repository;

import com.chatapp.chatappbackend.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
