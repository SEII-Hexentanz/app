package com.example.frontend.responseHandler;

import com.example.frontend.actions.DefaultAction;
import com.example.frontend.actions.PongAction;
import com.example.frontend.actions.UpdateStateAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.aau.payloads.Payload;
import at.aau.values.ResponseType;

public class ResponseHandler {
    private static final Map<ResponseType, Action> actions = new HashMap<>() {{
        put(ResponseType.PONG, new PongAction());
        put(ResponseType.UPDATE_STATE, new UpdateStateAction());
    }};

    public static void execute(ResponseType responseType, Payload payload) {
        Objects.requireNonNull(actions.getOrDefault(responseType, new DefaultAction())).execute(payload);
    }
}
