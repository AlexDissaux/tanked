package com.andigago.tanked.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefix for the message sent by J2E to the Client (subscription)
        config.enableSimpleBroker("/game", "/queue");
        // Prefix for the message sent by the Client to J2E (messages)
        config.setApplicationDestinationPrefixes("/app/", "/game/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Websocket endpoint
        // This is where the javascript interact with J2E
        registry.addEndpoint("/websocket").withSockJS().setInterceptors(new HttpSessionHandshakeInterceptor());
    }
}