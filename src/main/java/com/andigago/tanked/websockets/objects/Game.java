package com.andigago.tanked.websockets.objects;

import com.andigago.tanked.models.Map;
import lombok.Getter;

import java.util.HashMap;

public class Game {

    @Getter
    private String id;
    @Getter
    private java.util.Map<String, Tank> tanks = new HashMap<>();

    private int nextShotId = 0;

    @Getter
    public Map map;

    public Game(String id, Map map) {
        this.id = id;
        this.map = map;
    }

    public int getNewShot() {
        return nextShotId++;
    }

    public void addTank(String sessionId, Tank tank) {
        tanks.put(sessionId, tank);
    }
}
