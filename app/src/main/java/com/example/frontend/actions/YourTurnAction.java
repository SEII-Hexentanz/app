package com.example.frontend.actions;

import android.util.Log;

import com.example.frontend.Game;
import com.example.frontend.Player;
import com.example.frontend.responseHandler.Action;

import java.util.TreeSet;
import java.util.stream.Collectors;

import at.aau.payloads.Payload;
import at.aau.payloads.YourTurnPayload;

public class YourTurnAction implements Action {
    @Override
    public void execute(Game game, Payload payload) {
        if (payload instanceof YourTurnPayload yourTurnPayload) {
            game.setMyTurn();
            Log.i("App", "It is my turn");
        } else Log.e("App", "Payload is not an instance of YourTurnPayload");
    }
}
