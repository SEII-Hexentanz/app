package com.example.frontend.actions;

import android.util.Log;
import com.example.frontend.Game;
import com.example.frontend.responseHandler.Action;
import at.aau.payloads.Payload;
import at.aau.payloads.MoveSuccessfulPayload;

public class MoveSuccessfulAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof MoveSuccessfulPayload moveSuccessfulPayload) {
            Log.i("App", "Move successful: " + moveSuccessfulPayload.details());
            // Handle move successful action in game
        } else {
            Log.e("App", "Payload is not an instance of MoveSuccessfulPayload");
        }
    }
}
