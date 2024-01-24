package com.chatapp.chatappbackend.controllers;

import com.chatapp.chatappbackend.models.Chat;
import com.chatapp.chatappbackend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class UserServiceController {
//
//    public UserService userService;
//
//    @MessageMapping("/user.addUser")
//    @SendTo("/user/topic")
//    public User addUser(@Payload User user) {
//        userService.savUser(user);
//        return user;
//    }
//
//    @MessageMapping("/user.disconnectUser")
//    @SendTo("/user/topic")
//    public User disconnetUser(@Payload User user) {
//        userService.disconnetUser(user);
//        return user;
//    }
//
//    @GetMapping("/users")
//    public ResponseEntity findConnectedUsers(){
//        return new ResponseEntity(userService.findConnectedUsers(), null, HttpStatus.OK);
//    }

    @MessageMapping("/text")
    @SendTo("/chat/greetings")
    public String greeting() throws Exception {
        Thread.sleep(1000); // simulated delay
        return ("Hello, " + "message" + "!:" );
    }

    @MessageMapping("/hello")
    @SendTo("/chat/greetings")
    public String greeting(String message) throws Exception {
        System.out.println("message: " + message);
        return ("Received: " + message + "!");
    }

    @MessageMapping("/addChat")
    @SendTo("/chat/{roomId}")
    public Chat addChat(@DestinationVariable String roomId, StompHeaderAccessor accessor, @Payload Chat chat) throws Exception {
        System.out.println("Destination: " + accessor.getDestination());
        System.out.println("Chat: " + chat);
        return chat;
    }

}
