package com.andigago.tanked.websockets.listeners;

import com.andigago.tanked.util.WebsocketHelper;
import com.andigago.tanked.websockets.messages.ForGameMsg;
import com.andigago.tanked.websockets.objects.Game;
import com.andigago.tanked.websockets.objects.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class DisconnectListener  implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headers.getSessionId();

        Game game = gameRepository.removeSession(sessionId);

        if(game != null)
            WebsocketHelper.sendToGame(messagingTemplate, game.getId(), ForGameMsg.DELETE_TANK, sessionId);
    }
}