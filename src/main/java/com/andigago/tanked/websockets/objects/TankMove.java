package com.andigago.tanked.websockets.objects;

import lombok.Data;

@Data
public class TankMove {
    private float x;
    private float y;
    private int orientation = 0;
    private int canonOrientation = 0;
}
