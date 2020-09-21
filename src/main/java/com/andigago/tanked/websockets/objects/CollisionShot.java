package com.andigago.tanked.websockets.objects;

import lombok.Getter;
import lombok.Setter;

public class CollisionShot {
    @Getter
    @Setter
    private int shotId;
    @Getter
    @Setter
    private String touchedPlayerId;
    @Getter
    @Setter
    private String ownerId;

    public CollisionShot(){}
}
