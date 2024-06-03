package com.example.frontend.responseHandler;

import com.example.frontend.Game;
import com.example.frontend.actions.BadRequestAction;
import com.example.frontend.actions.DefaultAction;
import com.example.frontend.actions.DiceRolledAction;
import com.example.frontend.actions.PongAction;
import com.example.frontend.actions.UpdateStateAction;
import com.example.frontend.actions.YourTurnAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.aau.payloads.Payload;
import at.aau.values.ResponseType;

public class ResponseHandler {
    private ResponseHandler() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<ResponseType, Action> actions = new HashMap<>() {{
        put(ResponseType.PONG, new PongAction());
        put(ResponseType.UPDATE_STATE, new UpdateStateAction());
        put(ResponseType.BAD_REQUEST, new BadRequestAction());
        put(ResponseType.DICE_ROLLED, new DiceRolledAction());
        put(ResponseType.YOUR_TURN, new YourTurnAction());
    }};

    public static void execute(ResponseType responseType, Payload payload, Game game) {
        Objects.requireNonNull(actions.getOrDefault(responseType, new DefaultAction())).execute(game, payload);
    }
}
