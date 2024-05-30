package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.models.Player;
import at.aau.payloads.DicePayload;
import at.aau.payloads.Payload;

public class DiceRolledAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof DicePayload dicePayload) {
            int diceValue = dicePayload.diceValue();
            Player player = dicePayload.player();

            Log.i("App", "Player " + player.name() + " has rolled " + diceValue);

            game.diceRollAction((DicePayload) payload, game.getCurrentPlayer());
        } else Log.e("App", "Payload is not an instance of DicePayload");
    }
}
