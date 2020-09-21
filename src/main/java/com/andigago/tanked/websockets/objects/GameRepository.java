package com.andigago.tanked.websockets.objects;

import com.andigago.tanked.models.MapRepository;
import com.andigago.tanked.models.User;
import com.andigago.tanked.models.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameRepository {

    @Autowired
    private MapRepository mapRepository;
    @Autowired
    private UserRepository userRepository;

    @Getter
    private Map<String, Game> games = new HashMap<>();
    private Map<String, Game> gamesBySession = new HashMap<>();

    public Game getOrCreateById(String gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            com.andigago.tanked.models.Map randomMap = getRandomMap();
            game = new Game(gameId, randomMap);
            games.put(gameId, game);
        }
        return game;
    }

    // Game with gameId MUST exist !
    public Tank createSessionTank(String gameId, String sessionId, String playerName, User user) {
        Game game = games.get(gameId);
        gamesBySession.put(sessionId, game);

        Tank tank = Tank.generateNewTank(user, playerName);
        game.addTank(sessionId, tank);

        return tank;
    }

    public Game getBySession(String gameId) {
        return gamesBySession.get(gameId);
    }

    public Game removeSession(String sessionId) {
        Game game = gamesBySession.get(sessionId);

        if(game != null) {
            gamesBySession.remove(sessionId);
            game.getTanks().remove(sessionId);

            if(game.getTanks().size() == 0) {
                games.remove(game.getId());
                game = null;
            }
        }
        return game;
    }

    private com.andigago.tanked.models.Map getRandomMap() {
       return mapRepository.findOneRandom();
    }

    public void increasePoint(Tank tank) {
        tank.increasePoints();
        User user = tank.getUser();

        if(user != null) {
            user.increasePoints();
            userRepository.save(user);
        }
    }
}
