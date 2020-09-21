package com.andigago.tanked.websockets.objects;

import lombok.Getter;
import lombok.Setter;

public class Shot {
    @Getter
    @Setter
    private float x;
    @Getter
    @Setter
    private float y;
    @Getter
    @Setter
    private float vx;
    @Getter
    @Setter
    private float vy;
    @Getter
    @Setter
    private String owner = null;

    public Shot(){}
}
