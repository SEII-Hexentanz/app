package com.example.frontend.responseHandler;

import com.example.frontend.Game;
import com.example.frontend.actions.BadRequestAction;
import com.example.frontend.actions.CheatAction;
import com.example.frontend.actions.DefaultAction;
import com.example.frontend.actions.DiceRolledAction;
import com.example.frontend.actions.GameEndAction;
import com.example.frontend.actions.MoveCharacterAction;
import com.example.frontend.actions.NameExistsAction;
import com.example.frontend.actions.PlayerRegisteredAction;
import com.example.frontend.actions.PongAction;
import com.example.frontend.actions.UpdateStateAction;
import com.example.frontend.actions.YourTurnAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.aau.payloads.Payload;
import at.aau.values.ResponseType;

public class ResponseHandler {
    private final Map<ResponseType, Action> actions = new HashMap<>();

    public ResponseHandler() {
        actions.put(ResponseType.PONG, new PongAction());
        actions.put(ResponseType.UPDATE_STATE, new UpdateStateAction());
        actions.put(ResponseType.BAD_REQUEST, new BadRequestAction());
        actions.put(ResponseType.DICE_ROLLED, new DiceRolledAction());
        actions.put(ResponseType.YOUR_TURN, new YourTurnAction());
        actions.put(ResponseType.NAME_ALREADY_EXISTS, new NameExistsAction());
        actions.put(ResponseType.PLAYER_SUCCESSFULLY_REGISTERED, new PlayerRegisteredAction());
        actions.put(ResponseType.MOVE_SUCCESSFUL, new MoveCharacterAction());
        actions.put(ResponseType.GAME_END, new GameEndAction());
        actions.put(ResponseType.CHEAT_USED_PLAYER, new CheatAction());
    }

    public void execute(ResponseType responseType, Payload payload, Game game) {

        Objects.requireNonNull(actions.getOrDefault(responseType, new DefaultAction())).execute(game, payload);
    }
}
