package com.andigago.tanked.websockets.messages;

import lombok.Getter;

public class TypedMessage {

    @Getter
    private MessageType type;
    @Getter
    private Object payload;

    public TypedMessage(MessageType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public static TypedMessage create(MessageType type, Object... payload) {
        return new TypedMessage(type,payload);
    }
}
