package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.payloads.Payload;
import at.aau.payloads.PlayerMovePayload;

public class MoveCharacterAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof PlayerMovePayload playerMovePayload) {

            Game.INSTANCE.updateCharacterPosition(playerMovePayload.characterId(), playerMovePayload.newPosition(), playerMovePayload.moveType(), playerMovePayload.steps());
            Log.i("App", "Player has moved");

        } else Log.e("App", "Payload is not an instance of playerMovePayload");
    }
}
