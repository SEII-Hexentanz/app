package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.payloads.Payload;
import at.aau.payloads.UpdateStatePayload;

public class UpdateStateAction implements Action {
    @Override
    public void execute(Payload payload) {
        if (payload instanceof UpdateStatePayload updateStatePayload) {
            Game.INSTANCE.setGameState(updateStatePayload.game().gameState());
            Game.INSTANCE.setPlayers(updateStatePayload.game().players());
        } else Log.e("App", "Payload is not an instance of UpdateStatePayload");
    }
}
