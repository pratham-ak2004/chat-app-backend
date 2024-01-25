package com.chatapp.chatappbackend.controllers;

import com.chatapp.chatappbackend.models.Chat;
import com.chatapp.chatappbackend.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class UserServiceController {

    @Autowired
    private final ChatRepository chatRepository;

    @MessageMapping("/addChat/{roomId}")
    @SendTo("/chat/{roomId}")
    public Chat addChat(@DestinationVariable String roomId, StompHeaderAccessor accessor, @Payload Chat chat) throws Exception {

        // Save message to database
        chatRepository.save(chat);

        return chat;
    }

}
