package com.chatapp.chatappbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // create channels using /chat/senderId/receiverId
        config.enableSimpleBroker("/chat");
        // use /chatApp/messageMapPath to access the method
        config.setApplicationDestinationPrefixes("/chatApp");
        // use this mapping when you need to send to user specific channels
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // user ws://localhost:8080/api/socket
        registry.addEndpoint("/api/socket").setAllowedOrigins("*");
        registry.addEndpoint("/api/socket").setAllowedOrigins("*").withSockJS();
    }

}
