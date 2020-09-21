package com.andigago.tanked.websockets.listeners;

import com.andigago.tanked.models.User;
import com.andigago.tanked.util.WebsocketHelper;
import com.andigago.tanked.websockets.messages.ForGameMsg;
import com.andigago.tanked.websockets.messages.ForUserMsg;
import com.andigago.tanked.websockets.messages.TypedMessage;
import com.andigago.tanked.websockets.objects.Game;
import com.andigago.tanked.websockets.objects.GameRepository;
import com.andigago.tanked.websockets.objects.Tank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SubscribeListener {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameRepository gameRepository;

    @SubscribeMapping("game-{gameId}")
    @SendTo("/game/game-{gameId}")
    public TypedMessage onGameJoin(@Header("simpSessionId") String sessionId, @DestinationVariable String gameId, SimpMessageHeaderAccessor headerAccessor) {
        Game game = gameRepository.getOrCreateById(gameId);

        String tankName = null;
        User user = null;
        if(headerAccessor.getSessionAttributes() != null) {
            tankName = (String) headerAccessor.getSessionAttributes().get("tankName");
            user = (User) headerAccessor.getSessionAttributes().get("user");
        }

        Tank newTank = gameRepository.createSessionTank(gameId, sessionId, tankName, user);

        WebsocketHelper.sendToSession(messagingTemplate, sessionId, ForUserMsg.GAME_JOIN, game.getTanks(), game.getMap());
        return TypedMessage.create(ForGameMsg.NEW_TANK, sessionId, newTank);
    }
}
