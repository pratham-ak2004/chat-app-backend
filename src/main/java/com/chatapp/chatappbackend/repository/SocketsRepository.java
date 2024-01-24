package com.chatapp.chatappbackend.repository;

import com.chatapp.chatappbackend.models.Socket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SocketsRepository extends MongoRepository<Socket, String> {
    Socket findByParticipants(List<String> participants);

    Socket findByParticipantsContaining(String participant);
}
