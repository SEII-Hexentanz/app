package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;

import at.aau.payloads.Payload;
import at.aau.payloads.YourTurnPayload;

/**
 * This is a fallback action that is executed when the response type is not present in the predefined map.
 */
public class DefaultAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof YourTurnPayload){
            game.setMyTurn();
            Log.i("App", "it is my turn");
        }
        Log.e("App", "Response type not recognized");
    }
}
