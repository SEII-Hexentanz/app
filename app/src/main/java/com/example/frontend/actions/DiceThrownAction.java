package com.example.frontend.actions;

import android.util.Log;
import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;
import at.aau.payloads.Payload;
import at.aau.payloads.DicePayload;

public class DiceThrownAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof DicePayload diceThrownPayload) {
            Log.i("App", "Dice thrown: " + diceThrownPayload.getResult());
            // Handle dice thrown action in game
        } else {
            Log.e("App", "Payload is not an instance of DiceThrownPayload");
        }
    }
}
