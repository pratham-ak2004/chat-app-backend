package com.chatapp.chatappbackend.repository;

import com.chatapp.chatappbackend.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> getChatBySocketId(String socketId);
}
