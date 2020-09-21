package com.andigago.tanked.util;

import com.andigago.tanked.websockets.messages.ForUserMsg;
import com.andigago.tanked.websockets.messages.ForGameMsg;
import com.andigago.tanked.websockets.messages.TypedMessage;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketHelper {

    public static void sendToSession(SimpMessagingTemplate messagingTemplate, String sessionId, ForUserMsg type, Object... payload) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        messagingTemplate.convertAndSendToUser(sessionId,"/queue/onlyforme", new TypedMessage(type, payload),
                headerAccessor.getMessageHeaders());
    }

    public static void sendToGame(SimpMessagingTemplate messagingTemplate, String gameId, ForGameMsg type, Object... payload) {
        messagingTemplate.convertAndSend("/game/game-" + gameId, new TypedMessage(type, payload));
    }
}
