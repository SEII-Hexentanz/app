package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.models.Player;
import at.aau.payloads.CheatPayload;
import at.aau.payloads.Payload;

public class CheatAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof CheatPayload cheatpayload) {
            Player player = cheatpayload.player();

            Log.i("App", "Player " + player.name() + " has used the cheat");

            game.usedCheatAction(cheatpayload, game.getCurrentPlayer());
        } else Log.e("App", "Payload is not an instance of CheatPayload");
    }
}
