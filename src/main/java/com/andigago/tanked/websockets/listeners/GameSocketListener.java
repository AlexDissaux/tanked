package com.andigago.tanked.websockets.listeners;

import com.andigago.tanked.util.WebsocketHelper;
import com.andigago.tanked.websockets.messages.ForGameMsg;
import com.andigago.tanked.websockets.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameSocketListener {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameRepository gameRepository;

    @MessageMapping("MOVE_MY_TANK")
    public void onGameJoin(@Header("simpSessionId") String sessionId, TankMove tankMove) {
        Game game = gameRepository.getBySession(sessionId);

        if(game == null)
            return;

        Tank playerTank = game.getTanks().get(sessionId);
        playerTank.move(tankMove);
        WebsocketHelper.sendToGame(messagingTemplate, game.getId(), ForGameMsg.TANK_MOVE, sessionId, playerTank);
    }

    @MessageMapping("I_SHOOT")
    public void onShot(@Header("simpSessionId") String sessionId, Shot shot) {
        Game game = gameRepository.getBySession(sessionId);

        int shotId = game.getNewShot();
        shot.setOwner(sessionId);

        WebsocketHelper.sendToGame(messagingTemplate, game.getId(), ForGameMsg.NEW_SHOT, shotId, shot);
    }

    @MessageMapping("COLLISION_SHOT")
    public void onCollisionShot(@Header("simpSessionId") String sessionId, CollisionShot collisionShot) {
        Game game = gameRepository.getBySession(sessionId);

        Tank killedTank = null;
        if(collisionShot.getTouchedPlayerId() != null) {
            killedTank = game.getTanks().get(collisionShot.getTouchedPlayerId());
            killedTank.setRandomPosition();

            Tank killerTank = game.getTanks().get(collisionShot.getOwnerId());
            gameRepository.increasePoint(killerTank);
            WebsocketHelper.sendToGame(messagingTemplate, game.getId(), ForGameMsg.KILLED_TANK, collisionShot.getTouchedPlayerId(), killedTank,
                    collisionShot.getOwnerId());
        }

        WebsocketHelper.sendToGame(messagingTemplate, game.getId(), ForGameMsg.DELETE_SHOT, collisionShot.getShotId());
    }
}
