package com.andigago.tanked.websockets.messages;

public enum ForGameMsg implements MessageType {
    NEW_TANK,
    DELETE_TANK,
    TANK_MOVE,
    NEW_SHOT,
    DELETE_SHOT, KILLED_TANK,
}
